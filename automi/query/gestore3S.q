//This file was generated from (Commercial) UPPAAL 4.0.14 (rev. 5615), May 2014

/*

*/
cnt==3 --> cnt==0

/*

*/
cnt==0 --> cnt==3

/*

*/
E<> cnt>3

/*

*/
E<> cnt==3

/*

*/
A[] cnt==3 imply release

/*

*/
A[] Process(0).IN+Process(1).IN+Process(2).IN+Process(3).IN+Process(4).IN<=3

/*

*/
Process(0).REQ --> Process(0).IN

/*

*/
A[] forall(i:pid) Process(i).REQ imply Process(i).x<=8

/*

*/
A[] !deadlock
