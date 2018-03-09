% used with the scripts for testing in "batch mode"

:- initialization(all).

all :- argument_value(1,X), argument_value(2,Y), T=..[X,Y], call(T).

% w/ only 1 arg, it can be simply
% all :- argument_value(1,X), call(X).
