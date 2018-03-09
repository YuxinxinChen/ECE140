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
	


	
	


