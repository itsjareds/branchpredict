branchpredict
=============
### Authors:
* Jared Klingenberger <klinge2@clemson.edu>
* Shi Zheng <shiz@clemson.edu>

This project includes three branch prediction algorithm implementations in Java:

1. 2-bit Saturating Counter (BHT)
2. gshare with 2-bit Saturating Counter (PHT)
3. SAs scheme BTB (2-level adaptive branch prediction)

How to execute the programs
===========================
`gzcat server_trace_1_4M.txt.gz | java -jar saturatingcounter.jar`
`gzcat server_trace_1_4M.txt.gz | java -jar gshare.jar`
`gzcat server_trace_1_4M.txt.gz | java -jar branchtargetbuffer.jar`

Or, a convenience bash script has been included to run all three at once:
`sh run.sh`

Notes
=====
This project was written for CpSc 3300 (Computer Systems Organization) at Clemson University.
