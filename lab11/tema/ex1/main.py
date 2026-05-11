import asyncio
'''
async=semnalez ca e corutine
rez=await ceva() corutina curenta isi suspenda activitatea pana cand 
corutina ceva se termina
'''
async def main():
    coada=asyncio.Queue()

    valori=[5,10,20,50]
    for n in valori:
        await coada.put(n)

    async def calcul(nr):
        i=await coada.get()
        s=0
        for j in range(1,i+1):
            s+=j
        print(f"corutine {nr} , rez= {s}")
        coada.task_done()

    await asyncio.gather(#ruleaza simultan cele 4
        calcul(1),
        calcul(2),
        calcul(3),
        calcul(4)
    )

if __name__=='__main__':
    asyncio.run(main())