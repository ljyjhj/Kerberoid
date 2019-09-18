# Kerberoid (for a Higher Coverage of Decompiled Codes)

Kerberoid is a decompiler that properly merges partial results obtained from multiple decompilers into a final decompilation result to improve the coverage and the accuracy of decompiled codes. Now, Kerberoid is implemented with three decompilers(CFR, Jadx and JD Project).

This source codes are the demo for integrating the decompiled source codes of cfr, jadx and jdcore.
The whole source code of Kerberoid will be uploaded until September 30th.

## Requirement
* ssdeep
* scikit-learn
* joblib
* numpy

## ML Model

Kerberoid chooses the best candidate code block, which is similar to original source code, through a random forest classifier with the following three features. The first feature is the number of code lines making up each decompiled code block (e.g., function or class).  The second feature is the number of parameters used for a function. The third feature is the number of loop (e.g., for and while) or
conditional instructions (e.g., if and switch). Based on those three features, we construct a classifier to recommend the best candidate block.

Our random forest classifier model is uploaded in ml folder.

## Runing Kerberoid

Kerberoid will be released as jar file, which gets apk files as input and generates decompiled source codes. We uploaded the sample source codes from each decompiler in apps folder. The uploaded python file is a part of Kerberoid, which compares decompiled source codes from other decompilers and merges them. Decompiling process, which means that apk files are changed to decompiled source codes, will be uploaded until above date.

If you run this code the final result will be made in apps/app_name/src_extract/src_merged_all folder.
```
python merge.py
```

## Contact
If you experience any issues, you can ask for help by contacting us at heejun970@gmail.com.
