import random
import numpy
import functools

def to_java_arr(l):
    return ",".join(list(map(lambda x: str(x),l)))

size = 200
bound = 1000000
results = []
for i in range(0,10):
    l = list(range(1,bound))
    random.shuffle(l)
    l2 = l[:random.randrange(1,size)]
    average = numpy.mean(l2)
    toFlatten = list(map(lambda x: [x - 1, x + 1], l2))
    flatten = [b for a in toFlatten for b in a]
    flatten2 = [b for a in (list(map(lambda x: [x -1, 22, x + 3], l2))) for b in a]
    reduce1 = functools.reduce(lambda a, b: a + b, l2, 15)
    reduce2 = functools.reduce(lambda a, b: a - b, l2, 0)

    results.append("new int[]{{{}}},{},{},{},{},new int[]{{{}}},new int[]{{{}}}, new int[]{{{}}},{},{}".format(to_java_arr(l2), average, max(l2), min(l2), len(l2), to_java_arr(l2), to_java_arr(flatten), to_java_arr(flatten2), reduce1, reduce2))
    out = []
for i in range(0,len(results)):
    out.append("{{{}}}".format(str(results[i])))
print(",\n".join(out))