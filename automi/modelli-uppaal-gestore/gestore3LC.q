//This file was generated from (Academic) UPPAAL 4.1.18 (rev. 5444), November 2013

/*

*/
cnt==0 --> cnt==3

/*

*/
cnt==3 --> cnt==0

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
A[] !deadlock
