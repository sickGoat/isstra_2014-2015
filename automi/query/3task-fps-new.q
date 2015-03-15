//This file was generated from (Academic) UPPAAL 4.1.18 (rev. 5444), November 2013

/*

*/
A[] t5.W imply time[task2]<=10

/*

*/
A[] t3.W imply time[task1]<=5

/*

*/
A[] t1.W imply time[task0]<=2

/*

*/
A[] !deadlock

/*

*/
A[] M[0]<=1 && M[1]<=1 && M[2]<=1
