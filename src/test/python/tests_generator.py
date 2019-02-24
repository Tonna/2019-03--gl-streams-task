import random
import numpy
import functools

def to_java_arr(l):
    return ",".join(list(map(lambda x: str(x),l)))

def to_java_int(n):
    java_int_max = int((2**32)/2) - 1
    java_int_min = - int((2**32)/2)
    if n > java_int_max:
        return(to_java_int(n - 2**32))
    if n < java_int_min:
        return(to_java_int(n + 2**32))
    return n

size = 20
bound = 1000
results = []
for i in range(1,10):
    l = list(range(1,200500))
    random.shuffle(l)
    l2 = l[:random.randrange(1,size) + 1]
    average = numpy.mean(l2)
    toFlatten = list(map(lambda x: [x - 1, x + 1], l2))
    flatten = [b for a in toFlatten for b in a]
    flatten2 = [b for a in (list(map(lambda x: [x -1, 22, x + 3], l2))) for b in a]
    reduce1 = functools.reduce(lambda a, b: a + b, l2, 15)
    reduce2 = functools.reduce(lambda a, b: a - b, l2, 0)

    results.append("new int[]{{{}}},{},{},{},{},{},new int[]{{{}}},new int[]{{{}}}, new int[]{{{}}},{},{}".format(to_java_arr(l2), average, max(l2), min(l2), len(l2), to_java_int(sum(l2)), to_java_arr(l2), to_java_arr(flatten), to_java_arr(flatten2), to_java_int(reduce1), to_java_int(reduce2)))
    out = []
for i in range(0,len(results)):
    out.append("{{{}}}".format(str(results[i])))
print(",\n".join(out))
