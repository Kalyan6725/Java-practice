import numpy as np

a=np.array([1,2,3,4])

print(a.sum())
print(a.max())
print(a.min())

b=np.array([[1,2,3],[4,5,6]])
print(b.sum())
print(b.sum(axis=0))
print(b[1])
print(b[1][2])
print(b[1,2])
print(b[0:2,1:3])
otp=np.random.randint(1000,9999)
print(otp)