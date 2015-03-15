//This file was generated from (Commercial) UPPAAL 4.0.14 (rev. 5615), May 2014

/*

*/
i==0 --> i==5

/*

*/
i==4 --> i==5

/*

*/
i==3 --> i==4

/*

*/
i==2 --> i==3

/*

*/
i==1 --> i==2

/*

*/
i==0 --> i==1

/*

*/
E<> i==2 && T[0]==a && T[1]==a

/*

*/
E<> i==1 && T[0]==b

/*

*/
A[] i==5 imply T[0]==a && T[1]==b && T[2]==a && T[3]==a && T[4]==b
