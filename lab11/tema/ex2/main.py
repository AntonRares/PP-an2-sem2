import subprocess
def executa(cmd:str):
    cmds = [c.strip().split() for c in cmd.split('|')]#impart dupa |
    p = subprocess.Popen(
        cmds[0],
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE
    )
    for command in cmds[1:]:
        p_next=subprocess.Popen(
            command,
            stdin=p.stdout,#legatura pipeline
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )
        p.stdout.close()
        p=p_next
    out,err=p.communicate()
    print(f"output:{out.decode()}")
    print(f"err:{err.decode()}")
if __name__=='__main__':
    executa(input('comanda unix:'))
