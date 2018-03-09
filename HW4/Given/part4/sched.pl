np_names(N):-
	np(N,_,_).

np_names_not_yosemite(N):-
	np(N,_,_),
	N\=yosemite.

np_activities_yosemite(A):-
	np(yosemite,_,A).

np_states_yosemite(S):-
	np(yosemite,S,_).

np_states_grandcanyon(S):-
	np(grandcanyon,S,_).

np_states(N,S):-
	np(N,S,_).

np_sorted_activities_yosemite(SA):-
	np_activities_yosemite(SA),
	sort(SA).

np_single_state(N):-
	np(N,S,_),
	length(S,L),
	L<2.

np_multi_state(N):-
	np(N,S,_),
	length(S,L),
	L>1.

np_pair_names([N1, N2]):-
	np(N1,S1,_),
	length(S1,L1),
	L1<2,
	np(N2,S2,_),
	length(S2,L2),
	L2<2,
	S1==S2,
	N1@<N2.

np_2_state_2_activities(N):-
	np(N,S,A),
	length(S,L1),
	L1==2,
	length(A,L2),
	L2==2.

np_12_states_1or(N):-
	(np(N,S1,_),
	 length(S1,L1),
	 L1==1);
	(np(N,S2,_),
	 length(S2,L2),
	 L2==2).
	
np_12_states_2wo(N):-
	np(N,S,_),
	length(S,L),
	L==1.
	
np_12_states_2wo(N):-
	np(N,S,_),
	length(S,L),
	L==2.

np_camping_hiking_1or(N):-
	(np(N,_,A),
	 A == [camping,hiking]);
	(np(N,_,A),
	 A == [hiking,camping]).

np_camping_hiking_2wo(N):-
	np(N,_,A),
	member(A,[[hiking,camping],[camping,hiking]]).

np_camping_hiking_sort(N):-
	findall(N0,np_camping_hiking_2wo(N0),N1),
	sort(N1),
	member(N,N1).

insert(L,E,Z):- 
	append(L,[E],Z),sort(Z).

butlast(L,Z):-
        L=[H] -> Z=[];
        L=[H,_] -> Z=[H];
        (L=[H|T],
        butlast(T,R),
        Z = [H|R]).

naaa(L,NAL,AL):-
        L=[] -> (NAL=[],AL=[]);        
        (L=[H|T],
        naaa(T,R1,R2),
        (integer(H) -> (NAL=[H|R1], AL=R2);
        (\+(integer(H))) -> (NAL=R1, AL=[H|R2]))).

	
splitlist(L,Left,Pivot,Right):-
        L=[] -> fail;
        (\+(member(Pivot,L))) -> fail;
        (L=[H|T],
        (H\=Pivot -> (splitlist(T,R1,Pivot,R2), Left=[H|R1], Right=R2);
         H==Pivot -> (splitlistRight(T,R2), Right=R2, Left=[]))).

splitlistRight(L,R):-
        L=[] -> R=[];
        (L = [H|T],
         splitlistRight(T,R1),
         R=[H|R1]).
	
split3list(L,Owner,Left, Pivot, Right):-
        L=[] -> fail;
        (L=[H|T],
        ( (\+member(Owner, H)) -> (split3list(T,Owner, R1, P, R2), Left=[H|R1], Pivot=P, Right=R2);
          (splitlistRight(T,R2), Pivot=H, Right=R2, Left=[]))).

perm(L,PermL):-
        L=[]->PermL=[];
        (member(X,L),
        select(X,L,R1),
        perm(R1,R2),
        PermL=[X|R2]).


permsub(L,PermL):-
        L=[]->PermL=[];
        (naaa(L,NAL,_),
         member(X,L),
         ((member(X,NAL),NAL\=[X|_])->fail;
          (select(X,L,R1),
          permsub(R1,R2),
          PermL=[X|R2]))).
