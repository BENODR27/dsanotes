# mymath.pyx

def factorial(int n):
    cdef long result = 1
    cdef int i
    for i in range(2, n + 1):
        result *= i
    return result
