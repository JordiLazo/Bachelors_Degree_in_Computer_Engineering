
def multiplicacio(number1,number2):
        result = 0
        while number1 >= 1:
            if number1 % 2 != 0:
                result = result + number2
            number1 = int(number1 / 2)
            number2 = number2 * 2
        return result

def calcular_temps():
    import timeit
    temps = []
    for x in range(0,200,10):
        temps.append( (x, timeit.timeit("multiplicacio("+str(x)+",100)",
            setup="from __main__ import multiplicacio")) )
    return temps

def crear_grafica( x_list, y_list ):
    import matplotlib.pyplot as plt
    #import numpy as np

    #t = np.arange(x_list[0], x_list[-1])
    #t = np.dot(t, 0.05)
    #plt.plot(t, np.log2(t))
    plt.scatter(x_list, y_list)
    plt.show()

if __name__ == '__main__':
    temps = calcular_temps()
    crear_grafica(*map(list, zip(*temps)))
