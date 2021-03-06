from pyspark import SparkContext
import Sliding, argparse, math

def deserializeData(value):
    return tuple(value)

def serializeData(value):
    return ''.join(value)

def gen_bfs_flat_map(l):
    return lambda value: [value] + [(serializeData(child),l) for child in Sliding.children(WIDTH,HEIGHT,deserializeData(value[0]))] if value[1] == l - 1 else [value]

def bfs_flat_map(value):
    """ YOUR CODE HERE """
    return [serializeData(child) for child in Sliding.children(WIDTH,HEIGHT,value[0])]

def bfs_map(value):
    return (value, level)

def gen_bfs_map(lvl):
    return lambda value: (value, lvl)

def bfs_reduce(value1, value2):
    return min(value1, value2)

def solve_sliding_puzzle(master, output, height, width):
    """
    Solves a sliding puzzle of the provided height and width.
     master: specifies master url for the spark context
     output: function that accepts string to write to the output file
     height: height of puzzle
     width: width of puzzle
    """
    # Set up the spark context. Use this to create your RDD
    sc = SparkContext(master, "python")

    # Global constants that will be shared across all map and reduce instances.
    # You can also reference these in any helper functions you write.
    global HEIGHT, WIDTH, level

    # Initialize global constants
    HEIGHT=height
    WIDTH=width
    level = 1 # this "constant" will change, but it remains constant for every MapReduce job

    # The solution configuration for this sliding puzzle. You will begin exploring the tree from this node
    sol = Sliding.solution(WIDTH, HEIGHT)


    """ YOUR MAP REDUCE PROCESSING CODE HERE """
    dataAggregate = sc.parallelize([(serializeData(sol), 0)]);
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
    while curLen < maximum: # not really just temp value
        #data.cache()
        data = data.flatMap(bfs_flat_map) \
                   .distinct() \
                   .map(gen_bfs_map(level))
        dataAggregate += data
        level += 1
        if(level%k == 0):
            data = dataAggregate.partitionBy(p).reduceByKey(bfs_reduce)
            dataAggregate = data
            curLen = data.count()
    # """ YOUR OUTPUT CODE HERE """
    # # sort by value
    # output('\n'.join(data.map(lambda (x,y): str(x) + " " + tuple(str(y))).collect()))
    # # output("\n".join([str(level), str(curLen), str(maximum)]))
    pairs = data.collect()
    pairs=sorted(pairs, key = lambda value : value[1]) 


    """ YOUR OUTPUT CODE HERE """
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
    args = parser.parse_args()


    # open file for writing and create a writer function
    output_file = open(args.output, "w")
    writer = lambda line: output_file.write(line + "\n")

    # call the puzzle solver
    solve_sliding_puzzle(args.master, writer, args.height, args.width)

    # close the output file
    output_file.close()

# begin execution if we are running this file directly
if __name__ == "__main__":
    main()

