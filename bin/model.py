from operator import *
import numbers

class Vector:

	def __init__(self, n, arr=None):
		if n <= 0:
			raise RuntimeError('Dimension less than 1: {}'.format(n))

		if type(arr) is map:
			arr = list(arr)
		self.n = n
		self.arr = [0.]*n if arr is None else arr

		if len(arr) != n:
			raise RuntimeError('Passed array is not of dimension {}'.format(n))

	def __iter__(self):
		return iter(self.arr)

	def __getitem__(self, i):
		return self.arr[i]

	def __setitem__(self, i, v):
		self.arr[i] = v

	def __add__(self, v):
		if v.n != self.n:
			raise RuntimeError('Vectors are of dimensions {} and {}'.format(self.n, v.n))
		return Vector(self.n, map(add, self.arr, v.arr))

	def __sub__(self, v):
		if v.n != self.n:
			raise RuntimeError('Vectors are of dimensions {} and {}'.format(self.n, v.n))
		return Vector(self.n, map(sub, self.arr, v.arr))

	def __matmul__(self, v):
		if v.n != self.n:
			raise RuntimeError('Vectors are of dimensions {} and {}'.format(self.n, v.n))
		return Vector(self.n, map(mul, self.arr, v.arr))	

	def __mul__(self, v):
		if type(v) is int:
			return Vector(self.n, map(lambda x: x*v, self.arr))
		else:
			if v.n != self.n:
				raise RuntimeError('Vectors are of dimensions {} and {}'.format(self.n, v.n))
			return sum(self @ v)

	def __truediv__(self, x):
		if not isinstance(x, numbers.Number):
			raise TypeError('Expected a number but a {} was passed'.format(type(x)))
		return Vector(self.n, map(lambda i : i/x, self.arr))

	def __neg__(self):
		return Vector(self.n, map(neg, self.arr))

	def __str__(self):
		return self.arr.__str__()

# class Matrix:

# 	def __init__(self, n, m, matrix=None):
# 		if n <= 0 or m <= 0:
# 			raise RuntimeError('Dimension less than 1: {}'.format(n))

# 		if arr is not None:
# 			arr = list(arr)
# 		self.n = n
# 		self.arr = [0]*n if arr is None else arr
		
# 		if len(arr) != n:
# 			raise RuntimeError('Passed array is not of dimension {}'.format(n))


# x = Matrix([[1, 2], [3, 4]])
# y = Matrix([[2, 3], [5, 5]])

x = Vector(3, [2, 3, 4])
y = Vector(3, [5, 4, 2])

print(x + y)
print(x @ y)
print(x * y)
print(x - y)
print(-x)
print(-y)
print(x * 10)
print(x / 4)
x[1] = 2