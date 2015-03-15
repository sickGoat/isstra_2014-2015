//This file was generated from (Commercial) UPPAAL 4.0.14 (rev. 5615), May 2014

/*

*/
A[] (Barca.SA||Barca.SB) && moloA[0]+moloA[1]+moloA[2]==2 imply !(moloA[L]&&moloA[C]) && !(moloA[C]&&moloA[V])

/*

*/
A[] b[0]+b[1]+b[2]<=1

/*

*/
E<> goal()

/*

*/
A[] !deadlock
