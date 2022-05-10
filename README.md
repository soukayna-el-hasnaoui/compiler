
L’objectif du projet était de mettre en pratique les notions apprises dans le cours de techniques de compilation, notamment les phases d’analyses (lexical, syntaxique, sémantique), de génération de code cible et d'interprétation.


# **Grammaire Hors Contexte de notre langage** 

1. ***Programme*** 

Prog = ‘begin’ { instructions } ‘end’ 

2. ***Simple Expression*** 

SExpr = Ident | Entier | Reel 
3. ***Expression arithmetque*** 
ExprA = SExpr { opA SExpr } 

opA = (‘+’ | ‘-‘ | ‘\*’ | ‘/ ‘) 

4. ***Déclaration de variable*** 

DeclVar = type ident [ ‘=’ ExprA { ‘,’ ident [ ‘=’ ExprA] } ] ‘;’ 

5. ***Déclaration de constante*** 

DeclCste = ‘const’ type ident  ‘=’ ExprA { ‘,’ ident ‘=’ ExprA} ‘;’ 

6. ***Affectation*** 

Affectation = ident ‘=’ ExprA ‘;’ 
7. ***Structure alternative*** 
*//cettre grammaire etant ambigue, plusieurs arbres syntaxique possible pour un if* InstrIf = ‘if’ ‘(‘ condition ‘)’ ‘{‘ {instructions} ‘}’ [ partElse ] partElse = ‘else’  InstrIf | ‘{‘ {instructions} ‘}’  

//definition d’une structure exacte pour enlever l’ambiguité InstrIf = ‘if’ ‘(‘ condition ‘)’ ‘{‘ {instructions} ‘}’ partElse partElse = ‘else’  InstrIf | ‘{‘ {instructions} ‘}’  

8. ***Structure repetitive while*** 

InstrWhile = ‘while’ ‘(‘ condition ‘)’ ‘{‘ {instructions} ‘}’ 
9. ***Condition*** 
Condition = ExprA opC ExprA [ opL condition ] 

opC = ( ‘<’ | ‘>’ | ‘<=’ | ‘>=’ | ‘==’ | ‘ !=’) 

opL = (‘&&’ | ‘||’) 

10. ***Instruction de lecture*** 

InstrRead = scan ‘(‘ ident ‘)’ ‘;’ 

11. ***Instruction d’ecriture*** 

InstrWrite = print ‘(‘ ident ‘)’ ‘;’ 

12. ***Instruction*** 

Instr = DeclVar | DeclConst | Affectation | InstrWhile | InstrIf | InstrWrite | InstrRead | Cmt 




# **Contraintes Sémantiques du langage** 
13. ***Sur les variables*** 
- Deux variables ne peuvent pas avoir le même identifiant, idem pour les constantes. 
- Une variable non déclarer ou non initialiser ne peut pas apparaitre dans une expression (arithmétique ou booléenne). 
14. ***Sur les contraintes*** 
- Une constante ne peut être déclarer plus d’une fois et doit être initialiser lors de sa déclaration. 
- On ne peut pas affecter une valeur a une constante après sa déclaration. 


# **Unités lexicales** 


Les caractères spécieux simples : +, -, /, \*, %, <, >, =, {, }, (, ), \, “, ‘, [, ], ! 

Les caractères spécieux doubles : ==, ++, --, <=, >=, +=, -=, \*=, /=, &&, ||, !=, //, /\*, \*/ 

Les mots-clés: if, while, elseIf, const,  “;”, for, do, int, double, float, string, Boolean, else 

Les constantes littérales : 145, -78 : en générale tous les nombres, ‘a’, ‘b’ : en générale toutes les lettres. 

Les identificateurs : i, j, nom\_de\_variable 



Tables de correspondances de nos lexeme 

|+ |add |
| :- | :- |
|= |affect |
|- |sub |
|/ |div |
|% |mod |
|\* |mul |
|< |inf |
|> |sup |
|{ |acoladOuv |
|} |acoladFerm |
|[ |crocheOuv |
|] |crocheFerm |
|! |not |
|‘ |apostrof |
|>= |supEgal |
|<= |infEgal |
|== |egal |
|++ |inc |
|-- |dec |
|// |cmtLigne |
|&& |and |
||| |or |
|!= |diff |
|“ |griffe |
|if |si |
|else |sinon |
|elseIf |sinonSi |
|while |tantQue |
|const |constante |
|nom\_de\_variable |ident |
|; |Line End |
|145 |entier |
|-75 |entier |
|‘b’ |lettre |
|2,5 |reel |
|/\* |debCmt |
|\*/ |finCmt |
| | |


Grammaire de chaque unité lexical (basé sur celle du java) : lettre -> A | B ……. | Z | a | b ……. | z ó [a-zA-z]  chiffre -> 0 | 1 | 2 …… | 9 ó [0-9] ident -> lettre {[‘\_’] lettre | chiffre}  ó ^[a-zA-Z](\_ ?[a-zA-Z]){0,} nombre -> [signe]chiffre{chiffre} ó \_?[0-9]{1,} ; réel -> [ signe ] chiffre { chiffre } [ ‘,’ Chiffre { chiffre } ] 








