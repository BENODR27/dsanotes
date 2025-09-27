Absolutely! I can guide you through creating a **Python C extension** (a compiled module in C) and then using it in a **Flask web API**. Let’s do this step by step.

We’ll make a simple example where the C extension performs a basic operation (e.g., calculating factorial), and Flask exposes it via an API.

---

### **Step 1: Create a C extension**

1. **Create the C file** (`mymathmodule.c`):

```c
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
```

---

2. **Create `setup.py` to build the module:**

```python
from setuptools import setup, Extension

module = Extension('mymath', sources=['mymathmodule.c'])

setup(
    name='mymath',
    version='1.0',
    description='Example C extension module',
    ext_modules=[module]
)
```

---

3. **Build the extension:**

```bash
python setup.py build
python setup.py install


python setup.py build_ext --inplace (dev)
```

Now you can use it in Python:

```python
import mymath

print(mymath.factorial(5))  # Output: 120
```

---

### **Step 2: Use the C extension in Flask**

1. **Install Flask**:

```bash
pip install Flask
```

2. **Create `app.py`:**

```python
from flask import Flask, jsonify, request
import mymath  # our C extension

app = Flask(__name__)

@app.route('/factorial', methods=['GET'])
def factorial_api():
    try:
        n = int(request.args.get('n', 1))
        result = mymath.factorial(n)
        return jsonify({'number': n, 'factorial': result})
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True)
```

---

### **Step 3: Run the Flask API**

```bash
python app.py
```

Test it in the browser or Postman:

```
http://127.0.0.1:5000/factorial?n=5
```

**Response:**

```json
{
  "number": 5,
  "factorial": 120
}
```

---

✅ Now you have a working **C extension** integrated into a **Python Flask API**.

---

Got it ✅ — you want a **C extension (like your factorial example)** but instead of math, it should handle **image compression**.

We’ll do this in **C + Python.h** by using **Pillow (Python Imaging Library)** internally. That way you don’t need to write raw JPEG encoding logic.

---

## **Step 1: C Extension Code (`imagecompressmodule.c`)**

```c
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
```

---

## **Step 2: Setup Script (`setup.py`)**

```python
from setuptools import setup, Extension

module = Extension(
    'imagecompress',
    sources=['imagecompressmodule.c'],
)

setup(
    name='imagecompress',
    version='1.0',
    description='Image compression extension',
    ext_modules=[module],
)
```

---

## **Step 3: Build & Install**

> ⚠️ On Windows, you **still need MSVC Build Tools**. If you want to avoid them, we can rewrite this using **cffi**.

```bash
python setup.py build
python setup.py install
```

---

## **Step 4: Use in Python**

```python
import imagecompress

# Compress input.jpg -> output.jpg with quality=30
imagecompress.compress_image("input.jpg", "output.jpg", 30)
```

---

## **Step 5: Use in Flask**

```python
from flask import Flask, request, jsonify
import imagecompress

app = Flask(__name__)

@app.route('/compress', methods=['POST'])
def compress():
    try:
        input_path = request.form['input']
        output_path = request.form['output']
        quality = int(request.form.get('quality', 50))

        imagecompress.compress_image(input_path, output_path, quality)
        return jsonify({"status": "success", "output": output_path})
    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True)
```

---

⚠️ **Important:**

- This extension **depends on Pillow**. You must install it:

  ```bash
  pip install Pillow
  ```

- On Windows, **you need MSVC Build Tools** unless we use `cffi`.

---

