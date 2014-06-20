// $Id: hashmap.java,v 1.1 2008-02-05 18:18:43-08 - - $
//Written by me, Oliver Goldberg-Seder (ogoldber)
import java.util.*;
import java.io.*;
class node {
			byte[][] values;
			node link;
		}
class Global {
    public static int a;
	public static int b;
}
class sudoku{
//SETUP
	public static void main (String[] args) throws FileNotFoundException, java.io.IOException {
		byte increy;
		byte increx;
		node head;		
		int results;
		node thistable = new node();
		thistable.values =new byte[9][9];
		Scanner scan;
		if(args.length>0)scan=new Scanner(new FileInputStream(args[0]));
		else{ 
			scan= new Scanner(System.in);
			System.out.println("input values, 0=blank\narg1 may be file of 81 consecutive numbers, arg2 will prevent printing unless it is 'p' in which case it will only print # puzzles solved");
		}
		for(increy = 0; increy<9;increy++)
			for(increx = 0; increx<9;increx++){
				if(args.length==0)System.out.println("x= "+(increx+1)+"& y= "+(increy+1));
				thistable.values[increx][increy]= (byte)scan.nextInt();
			}
		//Main loop!
		head=thistable;
		Global.a=0;
		Global.b=0;
		while(head!=null){
			head=guesstime(head, args);
		}
		System.out.println("\n\ndone- "+Global.a+" @  %" +(double)100*Global.a/(Global.b+Global.a) + " efficiency");
	}

	public static void printnode(node first){
		System.out.print(" solution is found!");
		for(byte increy= 0; increy<9;increy++){
			System.out.println("");
			for(byte increx= 0; increx<9;increx++)
				System.out.print(first.values[increx][increy]);
		}
	}
	
	public static node guesstime(node choice, String[] args)   {
		boolean[] hasnumber = new boolean[10];
		byte buff;
		byte bufyf;
		byte y;
		byte x;
		boolean progressflag=false;
		boolean twodone=false;
		byte limit = 1;
		boolean haszero=false;
		byte count;
		boolean guessesmade;
		for(guessesmade=false,x=0;x<9 && limit<10;x++){
			for(y=0;y<9;y++){
				if(choice.values[x][y]!=0)continue;
				for(buff= 1;buff<10;buff++)
					hasnumber[buff]=false;
				for(buff= 0;buff<9;buff++){
					hasnumber[choice.values[buff][y]]=true;
					hasnumber[choice.values[x][buff]]=true;
				}
				byte roundx= (byte)((x/3)*3 +3);//round to next lowest 3 mult
				byte roundy= (byte)((y/3)*3 +3);
				for(buff = (byte)(roundx -3);buff<roundx;buff++)
					for(bufyf = (byte)(roundy -3);bufyf<roundy;bufyf++)
						hasnumber[choice.values[buff][bufyf]]=true;
				for(count =0, buff=1;buff<10;buff++)
					if(!hasnumber[buff])count++;
				if(count <= limit){
					for(buff=1;buff<=9;buff++){
						if(!hasnumber[buff]){
							guessesmade=true;
							progressflag=true;
							if(count>1){
								twodone=true;
								node next = new node();
								next.values=arraydup(choice.values);
								next.values[x][y] = buff;
								next.link=choice.link;
								choice.link=next;
								Global.b++;
							}
							else choice.values[x][y] = buff;
							
						}
					}
					if(twodone)return(choice.link);
				}
				if(choice.values[x][y]==0)haszero=true;
			}
			if(x==8 && y>= 8){
				x=0;
				if(guessesmade){guessesmade=false;continue;}
				limit++;
				continue;
			}
		}
		if(progressflag)return(choice);
		if(!haszero ){Global.a++;
						if(args.length<2){System.out.print("\n\nNumber "+Global.a);printnode(choice);}
						else if(args[1].equals("p"))System.out.print(Global.a+"\n");}
		
		return(choice.link);
	}

	public static byte[][] arraydup (byte[][] thearray){
		byte[][] next = new byte[9][9];
		byte thisx;
		byte thisy;
		for(thisx=0;thisx<9;thisx++)
			for(thisy=0;thisy<9;thisy++)
				next[thisx][thisy]=thearray[thisx][thisy];
		return(next);		
	}
}
