from setuptools import setup, Extension

module = Extension('mymath', sources=['mymathmodule.c'])

setup(
    name='mymath',
    version='1.0',
    description='Example C extension module',
    ext_modules=[module]
)
# from setuptools import setup, Extension

# module = Extension('imagecompress', sources=['imagecompressmodule.c'])

# setup(
#     name='imagecompress',
#     version='1.0',
#     description='Example C extension module',
#     ext_modules=[module]
# )
