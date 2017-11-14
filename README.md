# CountSystemBaseN
A base_N implementation of CountSystem.


This is a way of implementing a counting system with arbitrary base. For notation purposes, base N_n refers to the baseN implementation of a base n counting system. For example, base N_2 is NOT the same as binary or base _2, though it may look the same.


Each BaseN number is modeled as a collection of kNum objects, each of which holds a String value which represents its subscript (value). This subscript is a Decimal (_10) string, and any math done on the digits typically involves the methods implemented in CountSystem.


Some things to note:


The most efficient N-base for incrementing (experimentally determined) is n_9. It is, in fact, more efficient than Decimal (_10)


The most efficient N-base for multiplication or addition depends on the number that is being added or mupltiplied by (b). In general, the best option is a value of N which is an even divisor of b, that is still much smaller than b. Too large and it's basically just Decimal, too small and the advantage of using a divisor is pretty much lost.


In theory, the largest number that can be represented with this system (which is, in practice, far larger than any number that could ACTUALLY be stored by any machine) can be described as such:

{

The base of this number is a string of INT_MAX 9's. i.e. "999...999"
                                                          
it then follows that each digit of this number has its maximum value in the subscript, and that there are INT_MAX digits.

The subscript of each digit is, then, a string of INT_MAX-1 9's and one 8. i.e:

"999...998"

}

This number would take about 37 EB or 37 million TB to store. It's approximately equal to 10^10^10^10. There are 10 billion zeroes in the number which describes how many zeroes there are in that number.
