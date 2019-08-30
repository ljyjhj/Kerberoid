def trim_comment_cfr(wcode):
    ret = ""
    temp = wcode.split("\n")
    comment = False
    ckclass = False

    for i in range(len(temp)):
        if "/*" in temp[i]:
            comment = True

        if "*/" in temp[i]:
            comment = False
            if temp[i].index("*/")+2 == len(temp[i]):
                continue

        if "@Override" in temp[i]:
            continue

        if "package." in temp[i]:
            continue

        if "//" in temp[i] and not ("method has failed" in temp[i] and get_indent(temp[i]) == 8):
            continue

        if comment == True:
            continue

        if "class " in temp[i]:
            if " {" not in temp[i]:
                ckclass = True
                ret = ret + temp[i] + " "
                continue

        if ckclass == True and "{" in temp[i]:
            ret = ret+temp[i].lstrip(" ")+"\n"
        else:
            ret = ret+temp[i]+"\n"
        # print (temp[i])
        ckclass = False
    return ret

def trim_comment_jdgui(wcode):
    ret = ""
    temp = wcode.split("\n")
    comment = False
    ckclass = False

    for i in range(len(temp)):
        if "/*" in temp[i]:
            comment = True

        if "*/" in temp[i]:        #if len(overlapped_element(gt_file)):
            #print app
            #shutil.rmtree("gt/"+app)
            #shutil.rmtree("src_recover/"+app)
            #for element in overlapped_element(gt_file):
            #    print element, gt_file.count(element)
            #print ""
            comment = False
            if temp[i].index("*/")+2 == len(temp[i]):
                continue

        if "@Override" in temp[i]:
            continue

        if comment == True:
            continue

        if "//" in temp[i] and not ("Byte code:" in temp[i]):
            continue
            
        if "class " in temp[i] or "interface " in temp[i]:
            if " {" not in temp[i]:
                ckclass = True
                ret = ret + temp[i] + " "
                continue

        if ckclass == True:
            if "{" in temp[i]:
                ret = ret+temp[i].lstrip(" ")+"\n"
                ckclass = False
            else:
                ret = ret+temp[i].lstrip(" ") + " "
        else:
            if "{" == temp[i].lstrip(" "):
                ret = ret[:-1] + " " + temp[i].lstrip(" ") + "\n"
            else:
                ret = ret+temp[i]+"\n"

            # print i, ret

    return ret

def get_indent(s):
    return len(s)-len(s.lstrip())

def trim(func):
    ret = ""
    temp = func.split("\n")
    
    #print temp
    for i in range(len(temp)):
        line = temp[i]
        #print line
        #if "/*" in line:
        #    if "*/" in line:
        #        continue
        #    while True:
        #        line = f.readline()
        #        # print line
        #        if "*/" in line:
        #            line = f.readline()
        #            break
        #        else:
        #            continue
        if "//" in line: continue

        ret = ret+line
    ret = ret.replace("\r", "")
    ret = ret.replace(" ", "")
    ret = ret.replace("\n", "")
    ret = ret.replace("*/","")

    #print ret
    #print ""

    return ret


def parser_cfr(_file):
    f = open(_file) #, encoding="UTF-8")
    # print (cfr_files[j])
    #while True:
    #    line = f.readline()
    wcode = f.read().replace("\t","    ")
    # print wcode
    code = trim_comment_cfr(wcode)
    # print code
    codeline = code.split("\n")
    indent = 0

    codelist = []
    func = ""

    i = 0
    while i != len(codeline):
        if codeline[i] == "":
            i+=1
            continue
        if "{" in codeline[i] and get_indent(codeline[i]) == 4 and ";" not in codeline[i]:
            if "class " in codeline[i]:
                if not ("method has failed" in func and func.count("{") == 1):
                    codelist.append(func)

                func = ""
                in_func = ""
                in_codelist = []

                while True:
                    if "{}" in codeline[i]:
                        in_codelist.append(in_func+codeline[i]+"\n")
                        #print in_func
                        i+=1
                        break
                    elif "}" in codeline[i] and  get_indent(codeline[i]) == 4 and ";" not in codeline[i]:
                        in_codelist.append(in_func)
                        break
                    elif "{" in codeline[i] and get_indent(codeline[i]) == 8:
                        in_codelist.append(in_func)
                        in_func = ""
                    in_func+=codeline[i] + "\n"
                    i+=1

                in_var = in_codelist[:1]
                # print (in_var)
                in_temp = in_codelist[1:]
                #in_temp.sort()
                in_codelist = in_var+in_temp
                # print (func)
                for j in range(len(in_codelist)):
                    # print (func)
                    # print (in_codelist[j])
                    func = func+in_codelist[j]
                continue

            if "method has failed" not in func:
                if not ("Method not decompiled" in func and func.count("{") == 1):
                    codelist.append(func)

            func = ""
        if "}" in codeline[i] and get_indent(codeline[i]) == 0:
            i+=1
            continue
        func+=codeline[i] + "\n"
        i = i+1
    #print func
    if "method has failed" not in func:
        if not ("Method not decompiled" in func and func.count("{") == 1):
            codelist.append(func)
    #print func

    pack_impo = codelist[0:1]
    codelist = codelist[1:]

    split_pack_impo = pack_impo[0].split("\n")
    pack_impo_list = []
    varlist = []

    for i in range(len(split_pack_impo)):
        if "{" in split_pack_impo[i]:
            classcode = split_pack_impo[i]
            #print classcode
        elif get_indent(split_pack_impo[i]) == 0:
            pack_impo_list.append(split_pack_impo[i])
        elif get_indent(split_pack_impo[i]) == 4:
            varlist.append(split_pack_impo[i])
   
        #print split_pack_impo[i]
    pack_impo_list.sort()
    varlist.sort()
    temp = ""
    #print "\n",pack_impo[0]
    for i in range(len(pack_impo_list)):
        temp += (pack_impo_list[i] +"\n")

    try:
        temp+=classcode+"\n"
    except:
        pass

    for i in range(len(varlist)):
        temp += (varlist[i]+"\n")

    #print temp
    pack_impo.pop()
    pack_impo.append(temp)
    #codelist.sort()

    codelist = pack_impo + codelist

    return codelist

def parser_jdgui(_file):
    f = open(_file) #, encoding="UTF-8")
    # print (cfr_files[j])
    wcode = f.read()
    code = trim_comment_jdgui(wcode)
    #print code
    codeline = code.split("\n")
    indent = 0

    codelist = []
    func = ""

    i = 0
    while i != len(codeline):
        #print codeline[i]
        if codeline[i] == "":
            i+=1
            continue
        if "{" in codeline[i] and get_indent(codeline[i]) == 2 and ";" not in codeline[i]:
            if "class " in codeline[i]:
                codelist.append(func)
                func = ""
                in_func = ""
                in_codelist = []
                #print codeline[i]
                while True:
                    if "{}" in codeline[i]:
                        in_codelist.append(in_func+codeline[i]+"\n")
                        #print in_func
                        i+=1
                        break
                    elif "}" in codeline[i] and  get_indent(codeline[i]) == 2 and ";" not in codeline[i]:
                        # print in_func
                        in_codelist.append(in_func)
                        break
                    elif "{" in codeline[i] and get_indent(codeline[i]) == 4:
                        #print in_func
                        in_codelist.append(in_func)
                        in_func = ""
                    in_func+=codeline[i] + "\n"
                    #print in_func
                    i+=1

                in_var = in_codelist[:1]
                # print (in_var)
                in_temp = in_codelist[1:]
                #in_temp.sort()
                in_codelist = in_var+in_temp
                #print (func)
                for j in range(len(in_codelist)):
                    # print (func)
                    # print (in_codelist[j])
                    func = func+in_codelist[j]
                #print func
                continue
            codelist.append(func)
            #print func
            func = ""
        if "}" in codeline[i] and get_indent(codeline[i]) == 0:
            i+=1
            continue
        func+=codeline[i] + "\n"
        i = i+1

    codelist.append(func)

    pack_impo = codelist[0:1]
    codelist = codelist[1:]

    split_pack_impo = pack_impo[0].split("\n")
    pack_impo_list = []
    varlist = []

    for i in range(len(split_pack_impo)):
        if "{" in split_pack_impo[i]:
            classcode = split_pack_impo[i]
            #print classcode
        elif get_indent(split_pack_impo[i]) == 0:
            pack_impo_list.append(split_pack_impo[i])
        elif get_indent(split_pack_impo[i]) == 2:
            varlist.append(split_pack_impo[i])
   
        #print split_pack_impo[i]
    pack_impo_list.sort()
    varlist.sort()
    temp = ""
    #print "\n",pack_impo[0]
    for i in range(len(pack_impo_list)):
        temp += (pack_impo_list[i] +"\n")

    try:
        temp+=classcode+"\n"
    except:
        pass

    for i in range(len(varlist)):
        temp += (varlist[i]+"\n")

    #print temp
    pack_impo.pop()
    pack_impo.append(temp)
    #codelist.sort()
    #codelist.sort()

    codelist = pack_impo + codelist

    return codelist

def parser_jdcore(_file):
    jdcore_codelist = parser_jdgui(_file)
    match_indent = []
    
    for i in range(len(jdcore_codelist)):
        lines = jdcore_codelist[i].split("\n")
        temp = ""
        for j in range(len(lines)):
            temp +=" "*get_indent(lines[j])+lines[j]+"\n"
        match_indent.append(temp)

    return match_indent
    

'''
path = "/home/decom/multidecom/extract/src_recover/vector-camera/src_extract/src_jadx/org/xiph/libvorbis/vorbis_look_psy.java"

path1 = "/home/decom/multidecom/extract/gt/35c3-wifi-setup/app/src/main/java/nl/eventinfra/wifisetup/WifiSetup.java"

jadx = parser_cfr(path)
gt = parser_cfr(path1)

for i in range(len(jadx)):
    print i
    print jadx[i]
print "}"
'''
