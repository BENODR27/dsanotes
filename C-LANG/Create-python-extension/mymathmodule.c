#include <Python.h>

// Function: factorial
static PyObject* factorial(PyObject* self, PyObject* args) {
    int n;
    if (!PyArg_ParseTuple(args, "i", &n))
        return NULL;

    long result = 1;
    for (int i = 2; i <= n; i++)
        result *= i;

    return PyLong_FromLong(result);
}

// Method definitions
static PyMethodDef MyMathMethods[] = {
    {"factorial", factorial, METH_VARARGS, "Calculate factorial of a number"},
    {NULL, NULL, 0, NULL}
};

// Module definition
static struct PyModuleDef mymathmodule = {
    PyModuleDef_HEAD_INIT,
    "mymath",   // Module name
    NULL,       // Module documentation
    -1,
    MyMathMethods
};

// Module initialization
PyMODINIT_FUNC PyInit_mymath(void) {
    return PyModule_Create(&mymathmodule);
}
