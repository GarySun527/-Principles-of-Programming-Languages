package Project1;

public abstract class LexicalAnalyzer extends IO {
	public enum State { 
	  // non-final states     ordinal number
	
		Start,             // 0
		Bar,               // 1
		Ampersand,		   // 2
		E,                 // 3
		EPlusMinus,        // 4
		Period,			   // 5
		
	  // final states
	
		Add,				//6
		Mul,				//7
		LParen,				//8
		LBrace,				//9
		Colon,				//10
		Or,					//11
		And,				//12
		Sub,				//13
		Div,				//14
		RParen,				//15
		RBrace,				//16
		Semicolon,			//17
		Id,                 //18
		Int,                //19
		Float,              //20
		FloatE,             //21
		Inv,				//22
		Neq,				//23
		Assign,				//24
		Eq,					//25
		Lt,					//26
		Le,					//27
		Gt,					//28
		Ge,					//29
		KeyWord_if,			//30
		Keyword_else,     	//31
		Keyword_switch,		//32
		Keyword_case,		//33
		Keyword_default,	//34
		Keyword_while,		//35
		Keyword_do,			//36
		Keyword_for,		//37
		Keyword_print,		//38
		Keyword_false,		//39
		Keyword_true,		//40
		
		UNDEF;
	
		private boolean isFinal(){
			return ( this.compareTo(State.Add) >= 0 );  
		}	
	}
	
	public static String t; // holds an extracted token
	public static State state; // the current state of the FA
	
	private static int driver(){
		State nextSt; // the next state of the FA	
		t = "";
		state = State.Start;
	
		if ( Character.isWhitespace((char) a) )
			a = getChar(); // get the next non-whitespace character
		if ( a == -1 ) // end-of-stream is reached
			return -1;
	
		while ( a != -1 ){
			// do the body if "a" is not end-of-stream
			c = (char) a;
			nextSt = nextState( state, c );
			if ( nextSt == State.UNDEF ) // The FA will halt.
			{
				if ( state.isFinal() ){ // valid token extracted
					return 1; 
				}
				else // "c" is an unexpected character
				{
					t = t + c;
					a = getNextChar();
					return 0; // invalid token found
				}
			}else{
				// The FA will go on.
				state = nextSt;
				t = t + c;
				a = getNextChar();	
			}
		}
	
		// end-of-stream is reached while a token is being extracted
	
		if ( state.isFinal() )
			return 1; // valid token extracted
		else
			return 0; // invalid token found
	} // end driver
	
	public static void getToken(){
		// Extract the next token using the driver of the FA.
		// If an invalid token is found, issue an error message.
		int i = driver();
		if ( i == 0 )
			displayln(t + " : Lexical Error, invalid token");
	}
	
	private static State nextState(State s, char c){
		// Returns the next state of the FA given the current state and input char;
		// if the next state is undefined, UNDEF is returned.
		switch( state )
		{
		case Start:
			if ( Character.isLetter(c) )
				return State.Id;
			else if ( Character.isDigit(c) )
				return State.Int;
			else if ( c == '+' )
				return State.Add;
			else if ( c == '-' )
				return State.Sub;
			else if ( c == '*' )
				return State.Mul;
			else if ( c == '/' )
				return State.Div;
			else if ( c == '(' )
				return State.LParen;
			else if ( c == ')' )
				return State.RParen;
			else if ( c == '{')
				return State.LBrace;
			else if ( c == '}')
				return State.RBrace;
			else if ( c == ':')
				return State.Colon;
			else if ( c == ';')
				return State.Semicolon;
			else if ( c == '|')
				return State.Bar;
			else if ( c == '&')
				return State.Ampersand;
			else if ( c == '.')
				return State.Period;
			else if ( c == '!')
				return State.Inv;
			else if ( c == '=')
				return State.Assign;
			else if ( c == '<')
				return State.Lt;
			else if ( c == '>')
				return State.Gt;
			else
				return State.UNDEF;
		case Bar:
			if( c == '|')
				return State.Or;
			else 
				return State.UNDEF;
		case Or:
			if( c == '|')
				return State.Or;
			else
				return State.UNDEF;
		case Ampersand:
			if( c == '&')
				return State.And;
			else
				return State.UNDEF;
		case Id:
			if ( Character.isLetterOrDigit(c) )	
				return State.Id;
			else{
				if(t.compareTo("true") == 0)
					return State.Keyword_true;
				else if(t.compareTo("if") == 0)
					return State.KeyWord_if;
				else if(t.compareTo("else") == 0)
					return State.Keyword_else;
				else if(t.compareTo("switch") == 0)
					return State.Keyword_switch;
				else if(t.compareTo("case") == 0)
					return State.Keyword_case;
				else if(t.compareTo("default") == 0)
					return State.Keyword_default;
				else if(t.compareTo("while") == 0)
					return State.Keyword_while;
				else if(t.compareTo("do") == 0)
					return State.Keyword_do;
				else if(t.compareTo("for") == 0)
					return State.Keyword_for;
				else if(t.compareTo("print") == 0)
					return State.Keyword_print;
				else if(t.compareTo("false") == 0)
					return State.Keyword_false;
				else
					return State.UNDEF;
			}
		case Int:
			if ( Character.isDigit(c) )
				return State.Int;
			else if ( c == '.' )
				return State.Float;
			else
				return State.UNDEF;
		case Period:
			if ( Character.isDigit(c) )
				return State.Float;
			else
				return State.UNDEF;
		case Float:
			if ( Character.isDigit(c) )
				return State.Float;
			else if ( c == 'e' || c == 'E' )
				return State.E;
			else
				return State.UNDEF;
		case E:
			if ( Character.isDigit(c) )
				return State.FloatE;
			else if ( c == '+' || c == '-' )
				return State.EPlusMinus;
			else
				return State.UNDEF;
		case EPlusMinus:
			if ( Character.isDigit(c) )
				return State.FloatE;
			else
				return State.UNDEF;
		case FloatE:
			if ( Character.isDigit(c) )
				return State.FloatE;
			else
				return State.UNDEF;
		case Inv:
			if ( c == '=')
				return State.Neq;
			else 
				return State.UNDEF;
		case Assign:
			if( c == '=')
				return State.Eq;
			else 
				return State.UNDEF;
		case Lt:
			if( c == '=')
				return State.Le;
			else
				return State.UNDEF;
		case Gt:
			if( c == '=')
				return State.Ge;
			else 
				return State.UNDEF;
		case KeyWord_if:
			return State.UNDEF;
		case Keyword_else:
			return State.UNDEF;
		case Keyword_switch:
			return State.UNDEF;
		case Keyword_case:
			return State.UNDEF;
		case Keyword_default:
			return State.UNDEF;
		case Keyword_while:
			return State.UNDEF;
		case Keyword_do:
			return State.UNDEF;
		case Keyword_for:
			return State.UNDEF;
		case Keyword_print:
			return State.UNDEF;
		case Keyword_false:
			return State.UNDEF;
		case Keyword_true:
			return State.UNDEF;
		default:
			return State.UNDEF;
		}
	} // end nextState
	
	public static void main(String argv[]){		
		// argv[0]: input file containing source code using tokens defined above
		// argv[1]: output file displaying a list of the tokens 
	
		setIO( argv[0], argv[1] );	
		int i;	
		while ( a != -1 ) // while "a" is not end-of-stream
		{
			i = driver(); // extract the next token
			if ( i == 1 )
				displayln( t+"   : "+state.toString() );
			else if ( i == 0 )
				displayln( t+" : Lexical Error, invalid token");
		} 
	
		closeIO();
	}

}
