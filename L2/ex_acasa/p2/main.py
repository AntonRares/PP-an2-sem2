import os
import matplotlib.pyplot as plt
import numpy as np
from scipy import stats

def regLin(numeFisIn):
    # citire date din fisier
    data = np.loadtxt(numeFisIn)
    x = data[:, 0]
    y = data[:, 1]

    # regresie liniara
    slope, intercept, r, p, std_err = stats.linregress(x, y)
    myfunc = lambda x: slope * x + intercept
    mymodel = list(map(myfunc, x))

    # citire input de la tastatura
    filename = input("Nume fisier imagine=")#ceva .png
    folder = input("Calea unde se salveaza imaginea=")#/home/student/Desktop/
    point_color = input("Culoare puncte=")#red
    line_color = input("Culoare linie=")#blue

    # creeaza folderul daca nu exista
    os.makedirs(folder, exist_ok=True)

    # combina folder + nume fisier
    filepath = os.path.join(folder, filename)

    # plotare
    plt.scatter(x, y, color=point_color)#puncte
    plt.plot(x, mymodel, color=line_color)#linia data de regr lin

    # salvare imagine
    plt.savefig(filepath)
    # deschidere imagine cu sistem,ruleaza comada aia in shell
    os.system(f"xdg-open {filepath}")

def main():
    regLin("dataset.txt")

if __name__=="__main__":
    main()