(defun isLL2 (q)
	      (cond ((listp q) (= 2 (length q)))
		    (t nil)
	      )
) 

(defun isLL2a (q)
	      (cond ((listp q) (and (listp (cadr q)) (= 2 (length q)) ))
		    (t nil)
	      )
)

