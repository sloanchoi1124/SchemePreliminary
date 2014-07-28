(let ((modExp
       (lambda (base exponent modulus)
         (let ((modExpRec
                (lambda (a sq x)
                  (if (= x 0)
                      a
                      (let ((newA (if (odd? x)
                                      (remainder (* a sq) modulus)
                                      a))
                            (newSq (remainder (* sq sq) modulus))
                            (newX (quotient x 2))
                            )
                        (modExpRec newA newSq newX))))))
           (modExpRec 1 (remainder base modulus) exponent)
           )
         )))
  (modExp 2 100 101))