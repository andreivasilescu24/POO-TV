Nume: Vasilescu Andrei
Grupa: 324 CD

Proiectul are punctul de start in clasa Main unde se creeaza un nou obiect platforma ce va contine cate un obiect
pentru fiecare tip de pagina posibila (fiecarui tip de pagina ii corespunde cate o clasa) si campuri precum eroare
sau lista curenta de filme. Fiecare tip de pagina are un array de permisiuni in care sunt numele paginilor ce pot fi
accesate de acolo, pentru a putea afisa o eroare in cazul in care se incearca accesarea unei pagini nepermise. Dupa ce
se seteaza permisiunile pentru fiecare tip de pagina, incepe initializarea platformei prin navigarea catre homepage-ul
neautentificat.

**_ Design patterns _**

- Deoarece am folosit pattern-ul Visitor, fiecare tip de pagina va implementa cate doua interfete de visitor si visitable,
  astfel avand metode de accept/visit doar pentru tipurile de pagini ce o pot vizita/pe care le poate vizita. Astfel in
  momentul in care se schimba pagina printr-o comanda de tipul "change page" se va apela mai intai metoda "accept" a paginii
  pe care o vizitam, ce va apela metoda "visit" corespunzatoare a paginii ce viziteaza.

- De asemenea, am folosit si design pattern-ul Singleton, ce a fost implementat in clasele corespunzatoare paginilor, deoarece
  platforma va avea cate un obiect per fiecare tip de pagina, fiindca nu avem mai multe pagini de "register", de exemplu. Acest
  pattern a fost folosit si in clasa Platform deoarece poate fi doar un obiect "platforma".

* In cea de-a doua etapa a proiectului am adaugat:

- Design pattern-ul Builder in clasa "User" astfel acesta fiind folosit cand se creeaza un nou User.

- Design pattern-ul Strategy, ce este folosit pentru cele doua tipuri de filtrari posibile "Constains" si "Sort". Astfel, ambele
  tipuri de sortari au aceeasi strategie (de a filtra lista de filme dupa niste parametrii), dar au implementari diferite. Cele doua
  clase corespunzatoare tipurilor de filtrare implementeaza interfata "StrategyFilter" ce contine metoda "filter". Astfel, in momentul
  in care se doreste filtrarea filmelor pe pagina "Movies", se creeaza un obiect de tipul interfetei de strategie, iar apoi acestuia
  ii este asignat un nou obiect de tip "ContainsFilterStrategy" sau "SortFilterStrategy", in functie de filtrarea ceruta.

**_ Interfete _**
Cum am spus si mai sus ficare clasa ce corespunde unui tip de pagina va implementa doua interfete, una de visitor (cu metodele
de visit catre paginile in care poate ajunge user-ul din acea pagina) si una de visitable (cu metodele de accept de la paginile
din care se poate ajunge in acea pagina). De exemplu clasa "Login" va implementa interfetele "VisitorLogin" (cu metode de visit
catre homepage-ul autentificat si cel neautentifcat) si "VisitableLogin" (cu metode de accept de la homepage-ul neautentificat).

Tipurile de interfete sunt grupate in doua pachete diferite: unul pentru interfetele de visitor si unul pentru interfetele de
visitable.

- De asemenea cum am descris si mai sus in categoria "Design patterns" am adaugat o interfata "StrategyFilter" ce ajuta la design
  pattern-ul "Strategy"

**_ Clase _**

- Pachetul "input.data"
  In pachetul "input.data" putem gasi clasele ce ajuta la citirea input-ului din fisierele json, astfel vom avea clase de User,
  Action si Movie, dar si de Input ce va contine array-uri de obiecte din cele trei tipuri amintite asa cum sunt oferite
  in input. De asemenea, clasele User sau Action au campuri referitoare la obiecte de alt tip, precum "Credentials" sau
  "Filters" carora le corespund clase din acelasi pachet.

* Clasa Movie din acest pachet implementeaza interfata Comparable predefinita, suprascriind comparatorul din aceasta interfata
  cu unul "custom" dupa cerintele problemei (mai intai sorteaza dupa durata iar apoi dupa rating). Acest comparator va fi de ajutor
  in cazul unor actiuni de tip filter -> sort.

* Clasa User contine o clasa interna "Builder" ce va ajuta la realizarea design pattern-ului cu acelasi nume. Aceasta clasa are
  aceleasi campuri ca si clasa User si niste metode asemanatoare cu setterii, insa acestea vor returna obiectul dupa ce i s-a asignat
  unui camp valoarea corecta. De asemenea, va avea si metoda "build" ce va crea un nou User si il va returna, pe baza valorilor
  campurilor din Builder. Voi folosi clasa Builder cand creez un nou User.

- Pachetul "platform"
  Pachetul "platform" contine o singura clasa cu acelasi nume, aceasta avand rolul de a "hosta" toate paginile, continand si
  metode precum cea care afiseaza eroarea de pe ecran, cea de init ce aduce user-ul pe pagina de homepage neautentificat,
  cea care initializeaza permisiunile paginilor sau metodele de update:

* cea care updateaza baza de data cu filme curente si baza de date generala de filme.
* cea care updateaza user-ul curent al site-ului.
* cea care updateaza array-ul de actiuni primit in input (sterge ultima actiune executata) pentru ca metodele sa poata citi
  corect urmatoarea actiune ce trebuie executata.

\*\* Adaugat in cea de-a doua etapa:

- campul "pagesStack" ce va contine string-uri cu numele paginilor pe care a navigat user-ul curent dupa login/register, pentru
  a putea executa actiunea de "back" corect. Astfel de fiecare data cand se executa un "change page" cu succes, pagina de pe care
  s-a dat comanda va fi adaugata in "pagesStack", iar cand se va excuta cu succes o actiune de tip "back" ultima pagina din stack
  va fi stearsa.

- metoda "getRecommendation "ce returneaza notificarea cu recomandarea. In aceasta metoda se utilizeaza un TreeMap si un LinkedHashMap
  pentru a putea sorta corect perechile cheie-valoare, unde cheia este genul apreciat de utilizator, iar valoarea este numarul like-urilor date
  genului respectiv. Astfel, putem extrage recomandarea corecta, respectand indicatiile din enuntul temei.

- metoda "handleEmptyActions" ce va fi apelata cand se termina executarea tuturor actiunilor din array-ul de input, pentru a oferi recomandarea
  user-ului cu ajutorul metodei "getRecommendation".

- metoda "updateArrayOfMovies" ce va updata filmele, in cazul in care in baza de date de filme au avut loc schimbari ale unor filme.

-metoda "databaseAdd" ce va fi apelata daca se va adauga un film in baza de date si va returna un boolean, (true daca s-a adaugat filmul cu succes
sau false altfel).

-metoda "databaseDelete" ce va fi apelata daca se va sterge un film din baza de date si va sterge de asemenea filmul respectiv din toate array-urile
fiecarui user din baza de date unde acesta apare (purchasedMovies, watchedMovies, etc.). Daca filmul a fost cumparat de un user, acesta va fi si
notificat de stergerea acestuia si i se vor da inapoi tokeni sau un un film gratis, in functie de tipul contului.

-metoda "notifyAdd" ce va notifica toti userii din baza de date ce au dat subscribe la minim unul din genurile filmului ce tocmai a fost adaugat.
Aceasta metoda este apelata doar in urma unei adaugari cu succes a unui film in baza de date.

- Pachetul "pages"

* General Page
  Clasa ce este mostenita de clasele corespunzatoare tuturor tipurilor de pagini, continand campurile comune paginilor (user-ul
  curent si lista de permisiuni).

* HomepageAuthentified
  In aceasta clasa se gasesc:

- O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
- Metodele de accept si visit impuse de implementarea interfetelor
- Metoda de returnToHomepageAuthentified, ce revine pe aceasta pagina, este apelata
  in cazul unor erori
- Metoda ce adauga in lista curenta de filme, filmele care nu sunt banate in tara
  user-ului curent

* HomepageNotAuthentified
  In aceasta clasa se gasesc:

- O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
- Metodele de accept si visit impuse de implementarea interfetelor
- Metoda de returnToHomepageNotAuthentified, ce revine pe aceasta pagina, este apelata
  in cazul unor erori

* Login
  In aceasta clasa se gasesc:

- O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
- Metodele de accept si visit impuse de implementarea interfetelor
- Metoda de "login" ce realizeaza aceasta actiune, intorcand un boolean ce arata daca
  actiunea s-a realizat sau nu cu succes

* Logout
  In aceasta clasa se gasesc:

- Metodele de accept si visit impuse de implementarea interfetelor
- Metoda de "logout" ce realizeaza aceasta actiune, setand user-ul curent pe null si
  intorcandu-se pe pagina de "homepage neautentificat"

* Movies
  In aceasta clasa se gasesc:

- O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
- Metodele de accept si visit impuse de implementarea interfetelor
- Metoda de search ce executa aceasta actiune
- Metoda de filter ce executa aceasta actiune
- Metode de tip boolean "containsActors", "containsGenre" ce ajuta la filtrare
- Metoda de returnToMoviesPage, ce revine pe aceasta pagina, este apelata
  in cazul unor erori sau in urma unei actiuni de tip "on page"

\*\* cand se da comanda de "change page" catrea aceasta pagina se va reactualiza
lista de filme curente si se va afisa

- Register
  In aceasta clasa se gasesc:

* O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
* Metodele de accept si visit impuse de implementarea interfetelor
* Metoda de "register" ce realizeaza aceasta actiune, intorcand un boolean ce arata daca
  actiunea s-a realizat sau nu cu succes

- SeeDetails
  In aceasta clasa se gasesc:

* O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
* Metodele de accept si visit impuse de implementarea interfetelor
* Metoda de returnToSeeDetailsPage, ce revine pe aceasta pagina, este apelata
  in cazul unor erori sau in urma unei actiuni de tip "on page"
* Metoda SeeDetails ce afiseaza detaliile despre un film
* Metodele corespunzatoare actiunilor "on page": purchase, watch, like, rate, subscribe

\*\* Adaugat in cea de-a doua etapa:
Daca un User a dat deja rate unui film si mai vrea sa execute aceasta actiune o data, i
se va suprascrie primul rating, cu ajutorul unui array ce tine minte in ce ordine au dat userii
rating, un camp introdus in clasa "Movie", astfel fiecare film va avea un array de genul asociat.

- metoda de "subscribe" care verifica daca genul la care User-ul doreste sa dea "subscribe" este unul
  din genurile filmului actual de pe pagina "SeeDetails", altfel intorcand o eroare. Daca, insa, filmul contine
  genul, se va verifica daca user-ul deja a dat subscribe la acel gen, iar daca raspunsul este "da" se va intoarce
  o eroare. Daca insa verificarile celor doua conditii prezentate nu intorc o eroare, genul la care se da subscribe
  va fi adaugat in array-ul "subscribedGenres" corespunzator fiecarui user.

\*\* in metodele pentru actiuni "on page" orice schimbare adusa campurilor unui user
sau unui film, va crea un nou obiect de acel tip cu ajutorul unui copy constructor
ce va face o copie a obiectului vechi si ii va updata campurile ce tocmai au fost
schimbate, adaugandu-l apoi in baza de date dupa ce cel vechi a fost sters.

- Upgrades
  In aceasta clasa se gasesc:

* O metoda pentru interpretarea actiunilor cand user-ul se afla pe aceasta pagina
* Metodele de accept si visit impuse de implementarea interfetelor
* Metoda de returnToUpgradesPage, ce revine pe aceasta pagina, este apelata
  in cazul unor erori sau in urma unei actiuni de tip "on page"

\*\* la actiunile de upgrade orice schimbare adusa campurilor unui user va crea un nou
obiect de acel tip cu ajutorul unui copy constructor ce va face o copie a obiectului
vechi si ii va updata campurile ce tocmai au fost schimbate, adaugandu-l apoi in baza
de date dupa ce cel vechi a fost sters.

- Pachetul "strategy"
  Acest pachet ajuta la realizarea design pattern-ului "Strategy" pentru actiunea de filter. Am creat o interfata ce
  contine o metoda "filter" ce va fi implementata de fiecare din cele doua clase din acelasi pachet: ContainsFilterStrategy
  si SortFilterStrategy ce au fiecare o implementare diferita a metodei "filter" deoarece filtreaza array-ul de filme in
  moduri diferite.

\*\*\* Tot adaugat in cea de-a doua etapa:
Fiecare interpretor din clasele corespunzatoare tipurilor de pagini va putea interpreta acum actiuni de tip "back" si "database".

    * Actiunea de tip "back" va sterge ultimul element din "pagesStack" si va apela interpretorul de pe pagina pe care s-a dat
      back, daca exista o pagina valida in stack, iar daca stack-ul este gol va intoarce o eroare.
    * Actiunile de tip "database" vor apela metodele din clasa "Platform" descrise mai sus
