import os
import re
import json
import parser
import ssdeep

'''
jadx_pwd = "./Timber/src_jadx"
cfr_pwd = "./Timber/src_cfr"
jdcore_pwd = "./Timber/src_jdcore"
'''

def search_folder(folder):
    folder_list = []
    files = os.listdir(folder)
    for _file in files:
        f = folder+"/"+_file
        if os.path.isdir(f):
            if f not in folder_list:
                if "src_jadx" in folder:
                    folder_list.append(f[f.index("src_jadx/")+9:])
                elif "src_cfr" in folder:
                    folder_list.append(f[f.index("src_cfr/")+8:])
                elif "src_jdcore" in folder:
                    folder_list.append(f[f.index("src_jdcore/")+11:])
                elif "src_merged2" in folder:
                    folder_list.append(f[f.index("src_merged2/")+12:])
                elif "src_mergde_all" in folder:
                    folder_list.append(f[f.index("src_merged_all/")+15:])
                #folder_list.append(folder+"/"+_file)

            folder_list = folder_list+search_folder(folder+"/"+_file)
    return folder_list


def search_file(folder):
    file_list = []
    files = os.listdir(folder)
    for _file in files:
        if os.path.isdir(folder+"/"+_file):
            # print folder+"/"+_file
            file_list = file_list+search_file(folder+"/"+_file)
        else:
            if ".java" in _file:
                if "src_jadx" in folder:
                    f = folder+"/"+_file
                    file_list.append(f[f.index("src_jadx/")+9:])
                elif "src_cfr" in folder:
                    f = folder+"/"+_file
                    file_list.append(f[f.index("src_cfr/")+8:])
                elif "src_jdcore" in folder:
                    f = folder+"/"+_file
                    file_list.append(f[f.index("src_jdcore/")+11:])
                elif "src_merged2" in folder:
                    f = folder+"/"+_file
                    file_list.append(f[f.index("src_merged2/")+12:])
                elif "src_ground_truth" in folder:
                    f = folder+"/"+_file
                    file_list.append(f[f.index("src_ground_truth/")+16:])
                elif "src_merged_all" in folder:
                    f = folder+"/"+_file
                    file_list.append(f[f.index("src_merged_all/")+15:])
    return file_list


def folder_category(folder):
    if "src_jadx" in folder:
        return "jadx"
    elif "src_cfr" in folder:
        return "cfr"
    elif "src_jdcore" in folder:
        return "jdcore"
    elif "src_merged2" in folder:
        return "merged2"
    elif "src_merged_all" in folder:
        return "merged_all"

def ckpwd(filepath):
    return filepath[:filepath.rindex("/")]

def ckSchar(_str):
    if "!" in _str or "@" in _str or "#" in _str or "$" in _str or "-" in _str or "_" in _str:
        return True
    return False

def findfile(f1,f2_list):
    if f1 in f2_list:
        return True
    elif ckSchar(f1):
        f1_name = re.sub("[!@#$_-]","",f1.split("/")[-1])
        #print f1_name
        for f2 in f2_list:
            if ckpwd(f1) == ckpwd(f2):
                f2_name = re.sub("[!@#$_-]","",f2.split("/")[-1])
                if f2_name in f1_name or f1_name in f2_name:
                    return True
    
    return False
        

def compare_file(folder1, folder2):
    ret = dict()
    folder1_list = search_file(folder1)
    folder2_list = search_file(folder2)
    folder1_cat = folder_category(folder1)
    folder2_cat = folder_category(folder2)

    ret[folder1_cat+"_only"] = []
    ret[folder2_cat+"_only"] = []

    for lst in folder1_list:
        #if not findfile(lst,folder2_list):
        #    ret[folder1_cat+"_only"].append(lst)
        
        if lst not in folder2_list:
            #print (folder1_cat, "only")
            ret[folder1_cat+"_only"].append(lst)
        

    for lst in folder2_list:
        #if not findfile(lst,folder1_list):
        #    ret[folder2_cat+"_only"].append(lst)
        
        if lst not in folder1_list:
            # print (folder2_cat, "only")
            ret[folder2_cat+"_only"].append(lst)
        
    return ret

def isSamefunc(code1, code2):
    temp1 = code1
    temp2 = code2
    para1 = code1.split("\n")[0].replace("final ","")
    para2 = code2.split("\n")[0].replace("final ","")
    para1_lst = []
    para2_lst = []

    if "(" in temp1:
        temp1 = temp1[:temp1.index("(")]

    if "(" in para1 and ")" in para1:
        para1 = para1[para1.index("(")+1:para1.index(")")]

    if "(" in temp2:
        temp2 = temp2[:temp2.index("(")]

    if "(" in para2 and ")" in para2:
        para2 = para2[para2.index("(")+1:para2.index(")")]
    
    
    for para in para1.split(","):
        para1_lst.append(para.lstrip().split(" ")[0])
   
    for para in para2.split(","):
        para2_lst.append(para.lstrip().split(" ")[0])
    
    temp1 = temp1.split()
    temp2 = temp2.split()

    if "class" in temp1 and "class" in temp2:
        if temp1[temp1.index("class")+1] == temp2[temp2.index("class")+1]:
            return True

    if len(temp1) > 0 and len(temp2) > 0:
        if temp1[-1] == temp2[-1] and para1_lst == para2_lst:
            return True

    return False


def file_similarity(file1, file2):
    file1_cat = folder_category(file1)
    file2_cat = folder_category(file2)

    if file1_cat == "jadx":
        file1_codelist = parser.parser_cfr(file1)
    elif file1_cat == "cfr":
        file1_codelist = parser.parser_cfr(file1)
    elif file1_cat == "jdcore":
        file1_codelist = parser.parser_jdgui(file1)

    if file2_cat == "jadx":
        file2_codelist = parser.parser_cfr(file2)
    elif file2_cat == "cfr":
        file2_codelist = parser.parser_cfr(file2)
    elif file2_cat == "jdcore":
        file2_codelist = parser.parser_jdgui(file2)


    all_code = max(len(file1_codelist), len(file2_codelist))
    all_score = 0

    for file1_code in file1_codelist:
        temp = 0
        for file2_code in file2_codelist:
            if " {" not in file1_code or " {" not in file2_code:
                if isSamefunc(file1_code, file2_code):
                    h1 = ssdeep.hash(parser.trim(file1_code))
                    h2 = ssdeep.hash(parser.trim(file2_code))
                    score = ssdeep.compare(h1, h2)
                    all_score = all_score+score
                    # print(file1_code)
                    # print(file2_code)
                    # print(score)

            else:
                if isSamefunc(file1_code[:file1_code.index(" {")], file2_code[:file2_code.index(" {")]):
                # parser.trim(file1_code)
                    h1 = ssdeep.hash(parser.trim(file1_code))
                    h2 = ssdeep.hash(parser.trim(file2_code))
                    score = ssdeep.compare(h1, h2)

                    all_score = all_score + score
                    #print(file1_code)
                    #print(file2_code)
                    #print(score)

        # print (h_jadx)
        # print (h_cfr)

    return all_score/(all_code)

def compare_func(codelist1, codelist2):
    ret = []
    for code1 in codelist1:
        ck = False
        for code2 in codelist2:
            if isSamefunc(code1, code2):
                ck = True
                break

        if not ck:
            ret.append(code1)

    return ret


'''
f1 = search_folder(jadx_pwd)
f2 = search_folder(cfr_pwd)
comfolder = compare_file(cfr_pwd, jadx_pwd)

print (json.dumps(comfolder, indent=4, separators=(",",":")))

for jadx_file in f1:
    if jadx_file not in comfolder["jadx_only"]:
        print (jadx_file)
        print (file_similarity(jadx_pwd+"/"+jadx_file, cfr_pwd+"/"+jadx_file))
        print ("")
'''
# print (f1[65])
# print ("")

# print (file_similarity(jadx_pwd+"/"+f1[65], cfr_pwd+"/"+f2[65]))
