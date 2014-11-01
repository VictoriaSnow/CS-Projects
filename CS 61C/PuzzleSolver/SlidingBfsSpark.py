from pyspark import SparkContext
import Sliding, argparse, math
def deserializeData(value):
    return tuple(value)

def serializeData(value):
    return ''.join(value)

def bfs_flat_map(value):
    """ YOUR CODE HERE """
    return [Sliding.board_to_hash(WIDTH, HEIGHT, child) for child in Sliding.children(WIDTH,HEIGHT,Sliding.hash_to_board(WIDTH, HEIGHT, value[0]))]

# def gen_bfs_flat_map(l):
#     return lambda value: [value] + [(serializeData(child),l) for child in Sliding.children(WIDTH,HEIGHT,deserializeData(value[0]))] if value[1] == l - 1 else [value]

# def bfs_map(value):
#     return (value, level)

def gen_bfs_map(lvl):
    return lambda value: (value, lvl)

def bfs_reduce(value1, value2):
    return min(value1, value2)



def solve_puzzle(master, output, height, width, slaves):
    global HEIGHT, WIDTH, level
    HEIGHT=height
    WIDTH=width
    level = 0

    sc = SparkContext(master, "python")

    """ YOUR CODE HERE """
    solution=Sliding.solution(WIDTH, HEIGHT)
    sol = Sliding.board_to_hash(WIDTH, HEIGHT, solution)
    """ YOUR MAP REDUCE PROCESSING CODE HERE """
    # dataAggregate = sc.parallelize([(serializeData(sol), 0)]);
    dataAggregate = sc.parallelize([(sol, 0)]);
    data = dataAggregate
    curLen = 1

    #addFunc = lambda x, y: x + y
    maximum = math.factorial(HEIGHT*WIDTH)/2
    k=0 #count number
    p=0 #partition number
    if (maximum<13):
        k=16
        p=8
    elif(maximum<362880):
        k=32
        p=8
    else:
        k=56
        p=16
    while curLen < maximum: 

        data = data.flatMap(bfs_flat_map) \
                   .distinct() \
                   .map(gen_bfs_map(level))
        dataAggregate += data
        level += 1
        if(level%k == 0):
            data = dataAggregate.partitionBy(p).reduceByKey(bfs_reduce)
            dataAggregate = data
            curLen = data.count()
    # data.coalesce(6).saveAsTextFile(output)
    pairs = data.collect()
    # pairs=sorted(pairs, key = lambda value : value[1]) 


    """ YOUR OUTPUT CODE HERE """
    # pairs.coalesce(6).saveAsTextFile(output)
    # print(pairs)
    for pair in pairs:
        # print (pair)
        string=str(pair[1])+" "+str(tuple(pair[0]))
        output(string)
    sc.stop()



""" DO NOT EDIT PAST THIS LINE

You are welcome to read through the following code, but you
do not need to worry about understanding it.
"""

def main():
    """
    Parses command line arguments and runs the solver appropriately.
    If nothing is passed in, the default values are used.
    """
    parser = argparse.ArgumentParser(
            description="Returns back the entire solution graph.")
    parser.add_argument("-M", "--master", type=str, default="local[8]",
            help="url of the master for this job")
    parser.add_argument("-O", "--output", type=str, default="solution-out",
            help="name of the output file")
    parser.add_argument("-H", "--height", type=int, default=2,
            help="height of the puzzle")
    parser.add_argument("-W", "--width", type=int, default=2,
            help="width of the puzzle")
    parser.add_argument("-S", "--slaves", type=int, default=6,
            help="number of slaves executing the job")
    args = parser.parse_args()

    global PARTITION_COUNT
    PARTITION_COUNT = args.slaves * 16

    # call the puzzle solver
    solve_puzzle(args.master, args.output, args.height, args.width, args.slaves)

# begin execution if we are running this file directly
if __name__ == "__main__":
    main()
