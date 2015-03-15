/*

*/
ProcessoA(0).Req --> ProcessoA(0).Inside

/*

*/
E<> ( it_gest == 4 && ( accessi[0] == 3 || accessi[1] == 3 ))

/*

*/
A[] it_gest==4 imply ( accessi[0]!=3 || accessi[1]!=3)

/*

*/
E<> accessi[0]==2 && accessi[1]==-2

/*

*/
A[] ProcessoA(0).Inside imply !ProcessoB(1).Inside && ProcessoB(1).Inside imply !ProcessoA(0).Inside

/*

*/
E<> ProcessoA(0).Inside && ProcessoB(1).Inside

/*

*/
ProcessoB(1).Req --> ProcessoB(1).Inside

/*

*/
E<> deadlock





