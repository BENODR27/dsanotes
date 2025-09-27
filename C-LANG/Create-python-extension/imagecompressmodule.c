#include <Python.h>
#include <stdio.h>

// Function: compress_image(input_path, output_path, quality)
static PyObject* compress_image(PyObject* self, PyObject* args) {
    const char* input_path;
    const char* output_path;
    int quality;

    // Parse arguments (string, string, int)
    if (!PyArg_ParseTuple(args, "ssi", &input_path, &output_path, &quality)) {
        return NULL;
    }

    // Import Pillow inside C extension
    PyObject *pName, *pModule, *pFunc, *pArgs, *pValue;
    pName = PyUnicode_DecodeFSDefault("PIL.Image");  // import PIL.Image
    pModule = PyImport_Import(pName);
    Py_DECREF(pName);

    if (pModule != NULL) {
        pFunc = PyObject_GetAttrString(pModule, "open");  // PIL.Image.open
        if (PyCallable_Check(pFunc)) {
            pArgs = PyTuple_Pack(1, PyUnicode_FromString(input_path));
            pValue = PyObject_CallObject(pFunc, pArgs);
            Py_DECREF(pArgs);
            Py_DECREF(pFunc);

            if (pValue != NULL) {
                // call save with quality
                PyObject* saveFunc = PyObject_GetAttrString(pValue, "save");
                if (PyCallable_Check(saveFunc)) {
                    PyObject* kwargs = Py_BuildValue("{s:s, s:i}", "format", "JPEG", "quality", quality);
                    PyObject* saveArgs = PyTuple_Pack(1, PyUnicode_FromString(output_path));
                    PyObject* result = PyObject_Call(saveFunc, saveArgs, kwargs);

                    Py_XDECREF(result);
                    Py_DECREF(kwargs);
                    Py_DECREF(saveArgs);
                    Py_DECREF(saveFunc);
                    Py_DECREF(pValue);

                    Py_DECREF(pModule);
                    Py_RETURN_TRUE;
                }
                Py_DECREF(pValue);
            }
        }
        Py_DECREF(pModule);
    }

    PyErr_SetString(PyExc_RuntimeError, "Failed to compress image");
    return NULL;
}

// Method definitions
static PyMethodDef ImageCompressMethods[] = {
    {"compress_image", compress_image, METH_VARARGS, "Compress an image using Pillow"},
    {NULL, NULL, 0, NULL}
};

// Module definition
static struct PyModuleDef imagecompressmodule = {
    PyModuleDef_HEAD_INIT,
    "imagecompress",   // Module name
    NULL,              // Module documentation
    -1,
    ImageCompressMethods
};

// Module initialization
PyMODINIT_FUNC PyInit_imagecompress(void) {
    return PyModule_Create(&imagecompressmodule);
}
