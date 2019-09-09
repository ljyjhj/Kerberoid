# HybriDroid Better Performance Decompiler

HybriDroid is a decompiler that properly merges partial results obtained from multiple decompilers into a final decompilation result to improve the coverage and the accuracy of decompiled codes. Now, HybriDroid is implemented with three decompilers(CFR, Jadx and JD Project).

This source codes are the demo for integrating the decompiled source codes of cfr, jadx and jdcore.
The whole source code of HybriDroid will be uploaded until Septeber 30th.

## Requirement
* ssdeep
* scikit-learn
* joblib
* numpy

## Runing HybriDroid

HybriDroid will be released as jar file, which gets apk files as input and generates decompiled source codes. The uploaded python file is a part of HybriDroid, which compares decompiled source codes from other decompilers and merges them.

We uploaded the sample source codes from each decompiler in apps folder. If you run this code the final result will be made in apps/app_name/src_extract/src_merged_all folder.
```
python merge.py
```

## Contact
If you experience any issues, you can ask for help by contacting us at heejun970@gmail.com.
