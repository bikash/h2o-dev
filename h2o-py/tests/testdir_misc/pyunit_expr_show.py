import sys
sys.path.insert(1, "../../")
import h2o
from h2o.expr import Expr

def expr_show(ip,port):
    # Connect to h2o
    h2o.init(ip,port)

    iris = h2o.import_frame(path=h2o.locate("smalldata/iris/iris_wheader.csv"))
    print "iris:"
    iris.show()

    ###################################################################

    # expr[int], expr._data is pending
    res = 2 - iris
    res2 = res[0]
    print "res2:"
    res2.show()

    # expr[int], expr._data is remote
    res.eager()
    res3 = res[0]
    print "res3:"
    res3.show()

    # expr[int], expr._data is local
    expr = Expr([1,2,3])
    print "expr:"
    expr.show()

    # expr[tuple], expr._data is pending
    res = 2 - iris
    res4 = res[5,2]
    print "res4:"
    res4.show()

    # expr[tuple], expr._data is remote
    res.eager()
    res5 = res[5,2]
    print "res5:"
    res5.show()

    # expr[tuple], expr._data is local
    expr = Expr([[1,2,3], [4,5,6]])
    print "expr:"
    expr.show()

if __name__ == "__main__":
    h2o.run_test(sys.argv, expr_show)