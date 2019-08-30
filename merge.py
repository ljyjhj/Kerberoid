import parser
import compare
import ssdeep
import os
import json
import sys
import joblib
import numpy as np


rf_model = joblib.load("./ml/HybriDroid_04.pkl")
# return decompiler
def selector(test_data):
    test_data = np.array([test_data])

    return rf_model.predict(test_data)

############### count parameter number ##############
def paranum(s):
    temp = s.split("\n")[0]

    if "(" in temp and ")" in temp:
        temp = temp[temp.index("(")+1:temp.index(")")]
        if len(temp) < 2:
            return 0
        return len(temp.split(","))
    return 0

def accessmodi(s):
    temp = s.split("\n")[0].lstrip().split(" ")[0]
    return temp

def modi_jadx(s):
    temp = s.split("\n")
    ret = temp[0].replace("public", "protected") + "\n"

    for i in range(1,len(temp)):
        ret += temp[i] + "\n"

    return ret

#################### Merge Folder #####################
def create_merged_cfr_jadx_folder(jadx_list, cfr_list):
    path = appname+"/src_merged2"
    if not os.path.exists(path):
        os.mkdir(path)
    
    for i in range(len(jadx_list)):
        if not os.path.exists(path+"/"+jadx_list[i]):
            os.mkdir(path+"/"+jadx_list[i])
    for i in range(len(cfr_list)):
        if not os.path.exists(path+"/"+cfr_list[i]):
            os.mkdir(path+"/"+cfr_list[i])

def create_merged_all_folder(merged2_list, jdcore_list):
    path = appname+"/src_merged_all"
    if not os.path.exists(path):
        os.mkdir(path)
    
    for i in range(len(merged2_list)):
        if not os.path.exists(path+"/"+merged2_list[i]):
            os.mkdir(path+"/"+merged2_list[i])
    for i in range(len(jdcore_list)):
        if not os.path.exists(path+"/"+jdcore_list[i]):
            os.mkdir(path+"/"+jdcore_list[i])


################### Merge Code ########################
def merge_cfr_jadx(cfr_file_list):
    path = appname+"/src_merged2"
    comfolder = compare.compare_file(cfr_pwd, jadx_pwd)
    # print (json.dumps(comfolder, indent=4, separators=(",",":")))
     
    for f in comfolder["cfr_only"]:
        # print cfr_pwd+"/"+f
        cfr_codelist = parser.parser_cfr(cfr_pwd+"/"+f)

        wf = open(path+"/"+f,"a")
        for i in range(len(cfr_codelist)):
            wf.write(cfr_codelist[i])
        wf.write("}")
        wf.close()

    for f in comfolder["jadx_only"]:
        jadx_codelist = parser.parser_cfr(jadx_pwd+"/"+f)

        wf = open(path+"/"+f,"a")
        for i in range(len(jadx_codelist)):
            wf.write(jadx_codelist[i])
        wf.write("}")
        wf.close()

    for f in cfr_file_list:
        if f not in comfolder["cfr_only"]:
            jadx_codelist = parser.parser_cfr(jadx_pwd+"/"+f)
            cfr_codelist = parser.parser_cfr(cfr_pwd+"/"+f)

            cfr_common = []
            jadx_common = []

            for i in range(1,len(cfr_codelist)):
                for j in range(1,len(jadx_codelist)):
                    if compare.isSamefunc(cfr_codelist[i],jadx_codelist[j]):
                        cfr_common.append(i)
                        jadx_common.append(j)
                        break

            wf = open(path+"/"+f,"a")
            
            if len(cfr_codelist) > 0 and len(jadx_codelist) > 0:
                if len(cfr_codelist[0]) < len(jadx_codelist[0]):
                    wf.write(cfr_codelist[0])
                else:
                    wf.write(jadx_codelist[0])
            
            cfr_score = 0
            jadx_score = 0
            for i in range(0,len(cfr_common)):
                temp_cfr = cfr_codelist[cfr_common[i]]
                temp_jadx = jadx_codelist[jadx_common[i]]

                cfr_if = temp_cfr.count("if (")
                jadx_if = temp_jadx.count("if (")
                jdcore_if = -1
                cfr_switch = temp_cfr.count("switch ")
                jadx_switch = temp_jadx.count("switch ")
                jdcore_switch = -1
                cfr_for = temp_cfr.count("for ")
                jadx_for = temp_jadx.count("for ")
                jdcore_for = -1
                cfr_while = temp_cfr.count("while ")
                jadx_while = temp_jadx.count("while ")
                jdcore_while = -1
                cfr_len = len(temp_cfr)
                jadx_len = len(temp_jadx)
                jdcore_len = -1
                numpara = paranum(temp_cfr)

                test_data = [cfr_if,jadx_if,jdcore_if,cfr_switch,jadx_switch,jdcore_switch,cfr_for,jadx_for,jdcore_for,cfr_while,jadx_while,jdcore_while,cfr_len,jadx_len,jdcore_len,numpara]

                select_decom = selector(test_data)
                if accessmodi(temp_cfr) == "protected" and accessmodi(temp_jadx) == "public":
                    temp_jadx = modi_jadx(temp_jadx)
                
                if select_decom[0] == 0:
                    #print(temp_cfr)
                    wf.write(temp_jadx)
                elif select_decom[0] == 1:
                    #print (temp_cfr)
                    wf.write(temp_cfr)
                elif select_decom[0] == 2:
                    wf.write(temp_jadx)
                else:
                    wf.write(temp_jadx)


                #print (select_decom)
                #print (test_data)

            for i in range(1,len(cfr_codelist)):
                if i not in cfr_common:
                    wf.write(cfr_codelist[i])

            
            for i in range(1,len(jadx_codelist)):
                if i not in jadx_common:
                    wf.write(jadx_codelist[i])
                    
            wf.write("}")
            wf.close()




def merge_all(merged_file_list): # cfr_file_list):
    path = appname+"/src_merged_all"
    comfolder = compare.compare_file(merged_pwd, jdcore_pwd)
    # print (json.dumps(comfolder, indent=4, separators=(",",":")))
    
    for f in comfolder["merged2_only"]:
        # print cfr_pwd+"/"+f
        merged_codelist = parser.parser_cfr(merged_pwd+"/"+f)

        wf = open(path+"/"+f,"a")
        for i in range(len(merged_codelist)):
            wf.write(merged_codelist[i])
        wf.write("}")
        wf.close()

    for f in comfolder["jdcore_only"]:
        jdcore_codelist = parser.parser_jdcore(jdcore_pwd+"/"+f)

        wf = open(path+"/"+f,"a")
        for i in range(len(jdcore_codelist)):
            #print jdcore_codelist[i]
            wf.write(jdcore_codelist[i])
        wf.write("}")
        wf.close()
    
    for f in merged_file_list:
        if f not in comfolder["merged2_only"]:
            merged_codelist = parser.parser_cfr(merged_pwd+"/"+f)
            jdcore_codelist = parser.parser_jdcore(jdcore_pwd+"/"+f)

            merged_common = []
            jdcore_common = []
            
            for i in range(1,len(merged_codelist)):
                for j in range(1,len(jdcore_codelist)):
                    if compare.isSamefunc(merged_codelist[i],jdcore_codelist[j]):
                        merged_common.append(i)
                        jdcore_common.append(j)
                        break
            
            wf = open(path+"/"+f,"a")
            #print "----------------- package --------------------"
            if len(merged_codelist) > 0 and len(jdcore_codelist) > 0:
                if len(merged_codelist[0]) < len(jdcore_codelist[0]):
                    wf.write(merged_codelist[0])
                else:
                    wf.write(jdcore_codelist[0])
            
            #print "----------------- common ---------------------"
            
            cfr_score = 0
            jadx_score = 0
            jdcore_score = 0
            for i in range(0,len(merged_common)):
                temp_merged = merged_codelist[merged_common[i]]
                temp_jdcore = jdcore_codelist[jdcore_common[i]]

                cfr_if = temp_merged.count("if (")
                jadx_if = temp_merged.count("if (")
                jdcore_if = temp_jdcore.count("if (")
                cfr_switch = temp_merged.count("switch ")
                jadx_switch = temp_merged.count("switch ")
                jdcore_switch = temp_jdcore.count("if (")
                cfr_for = temp_merged.count("for ")
                jadx_for = temp_merged.count("for ")
                jdcore_for = temp_jdcore.count("if (")
                cfr_while = temp_merged.count("while ")
                jadx_while = temp_merged.count("while ")
                jdcore_while = temp_jdcore.count("if (")
                cfr_len = len(temp_merged)
                jadx_len = len(temp_merged)
                jdcore_len = len(temp_jdcore)
                numpara = paranum(temp_merged)

                test_data = [cfr_if,jadx_if,jdcore_if,cfr_switch,jadx_switch,jdcore_switch,cfr_for,jadx_for,jdcore_for,cfr_while,jadx_while,jdcore_while,cfr_len,jadx_len,jdcore_len,numpara]

                select_decom = selector(test_data)

                if accessmodi(temp_jdcore) == "protected" and accessmodi(temp_merged) == "public":
                    temp_merged = modi_jadx(temp_merged)

                if select_decom[0] == 0:
                    wf.write(temp_merged)
                elif select_decom[0] == 1:
                    wf.write(temp_merged)
                elif select_decom[0] == 2:
                    wf.write(temp_merged)
                else:
                    wf.write(temp_jdcore)

            #print "---------------merged not common -------------"

            for i in range(1,len(merged_codelist)):
                if i not in merged_common:
                    wf.write(merged_codelist[i])
            #print "---------------jdcore not common -------------"
            for i in range(1,len(jdcore_codelist)):
                if i not in jdcore_common:
                    wf.write(jdcore_codelist[i])
                    
            wf.write("}")
            wf.close()

apppath = "./apps"
applist = os.listdir(apppath)
#test_apps = open("test_apps.txt").read().split("\n")

cnt = 0

for app in applist:    
    appname = apppath+"/"+app+"/src_extract"
    #print appname

    if os.path.exists(appname + "/src_merged2"):
        continue

    print(app)

    jadx_pwd = appname+"/src_jadx"
    cfr_pwd = appname+"/src_cfr"
    jdcore_pwd = appname+"/src_jdcore"
    merged_pwd = appname+"/src_merged2"
    
    if not os.path.exists(cfr_pwd):
        os.mkdir(cfr_pwd)

    folder1 = compare.search_folder(jadx_pwd)
    folder2 = compare.search_folder(cfr_pwd)

    file1 = compare.search_file(jadx_pwd)
    file2 = compare.search_file(cfr_pwd)

    create_merged_cfr_jadx_folder(folder1,folder2)
    merge_cfr_jadx(file2)

    merged2_folder = compare.search_folder(merged_pwd)
    jdcore_folder = compare.search_folder(jdcore_pwd)

    merged_file = compare.search_file(merged_pwd)
    jdcore_file = compare.search_file(jdcore_pwd)

    create_merged_all_folder(merged2_folder, jdcore_folder)
    merge_all(merged_file)
  


