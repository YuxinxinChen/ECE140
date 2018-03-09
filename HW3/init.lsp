(load "s.l")
(load "../test.l")

(setq *print-case* :downcase)

(defmacro pp (x) (pprint (symbol-function x)))

; just type 
;    (load "../init.lsp")
; to reload everything that init.lsp does
; or, for less typing, use this function and just type
;     (loi)
(defun loi () (load "../init.lsp"))

