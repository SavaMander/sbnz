# Sistem za sprečavanje zloupotrebe platformi za dostavu hrane

## Članovi tima

Miroslav Blagojević SV43/2021

Sava Janjić SV51/2021

## Motivacija

Iz iskustva sa radom i korišćenjem više platformi za dostavu hrane,
uočeno je da trenutno najpopularnije platforme primenjuju jako
jednostavne mehanizme zaštite od korisničke zloupotrebe i relativno ih
je lako eksploatisati.

## Pregled problema

Sistem će uživo pratiti sve dostave i korisnike aplikacije u jednom
gradu, beležiti sumnjive događaje i reagovati na njih po definisanim
pravilima.

Dok većina korisnika koristi platformu u skladu sa njenom namenom,
postoji manjina koja aktivno traži i eksploatiše sistemske slabosti.

### Potencijalne vrste zloupotrebe: ###

Iskorišćavanje promo kodova - Ovo je jedna od najčešćih i najdirektnijih
formi finansijske zloupotrebe. Promotivne ponude se sistematski
iskorišćavaju, najčešće one namenjene novim korisnicima (\"popust na
prvu porudžbinu\"), kako bi ostvarili neograničene popuste.

Zlonamerno otkazivanje porudžbine - korisnik namerno otkazuje
porudžbinu kako bi ukoliko je već spremna mogao da je preuzme od
dostavljača po dogovoru. Ova vrsta prevare predstavlja sofisticiraniji
napad i često uključuje dogovor između korisnika i dostavljača. Cilj je
dobiti pripremljenu hranu besplatno.

Reklamaciona prevara - Korisnik lažno prijavljuje problem sa
porudžbinom koju je uredno primio, sa ciljem da dobije potpuni ili
delimični povraćaj novca, kredit za buduću kupovinu ili potpuno novu
porudžbinu besplatno.

## Metodologija rada

U sistemu postoje tri korisnika:

-   Mušterija (Korisnik)

-   Admin

-   Korisnička podrška
 
### Mušterija (Korisnik)

-   Kreira nalog

-   Kreira porudžbinu

-   Otkazuje porudžbinu

-   Šalje žalbu korisničkoj službi

-   Unosi promocionalne kodove

### Administrator

-   Trajno blokira korisnika

-   Ručno deblokira adrese koje su flagovane kao sumnjive

-   Trajno blokira adrese ukoliko ima potrebe za tim

-   Ima uvid u istoriju porudžbina korisnika

-   Ima uvid u istoriju prethodnih sumnjivih događaja vezanih za nalog
    korisnika

-   Ima uvid u sve adrese koje sistem flaguje

### Korisnička podrska

-   Prima žalbe korisnika

-   Flaguje sumnjive zalbe koje dalje sistem procesuira

Ostali entiteti u sistemu - Porudžbina, Adresa, IP adresa, Kreditna
kartica

## Baza znanja

### 3 nivoa sumnjivosti korisnika

1.  Validan korisnik

2.  Potencijalno sumnjiv korisnik - moguće kratkotrajno blokiranje

3.  Prethodno blokiran korisnik - moguće poslati adminu zahtev za
    trajno blokiranje

Osim korisnika, kreditna kartica, adresa i IP adresa takođe mogu biti
flagovani kao sumnjivi.

### Pravila vezana za korisnički nalog (prilikom kreiranja naloga i kreiranja porudžbine):

- Ukoliko je korisnik na 1. nivou, novokreiran, i uneo je više od jednog
promo koda: Nalog se postavlja na 2. nivo.

- Ukoliko je korisnički nalog na 1. nivou i IP adresa je flagovana kao
sumnjiva:\
Nalog se postavlja na 2. nivo

- Ukoliko je korisnički nalog na 1. nivou i adresa je flagovana kao
sumnjiva:\
Nalog se postavlja na 2. nivo

- Ukoliko je korisnički nalog na 1. nivou i kartica je flagovana kao
sumnjiva:\
Nalog se postavlja na 2. Nivo

- Ukoliko je korisnički nalog na 1. nivou i koristi temporary mail:\
Nalog se postavlja na 2. nivo, korisnik se kratkotrajno blokira

- Ukoliko je korisnički nalog novokreiran i kartica je već koriscena na 3
različita naloga:\
Nalog se postavlja na 2. nivo, kartica se flaguje kao sumnjiva

- Ukoliko je korisnički nalog na 1. nivou i korisnik je otkazao
porudžbinu:\
Nalog se postavlja na 2. nivo

- Ukoliko je korisnički nalog na 2. nivou i korisnik je otkazao
porudžbinu:\
Nalog se postavlja na 3. nivo i kratkotrajno blokira

- Ukoliko je korisnički nalog na 2. nivou i korisnik je menjao adresu:
Nalog se postavlja na 3. nivo

- Ukoliko je korisnički nalog novokreiran, na drugom nivou i korisnik je
uneo više od 1 promo koda: Nalog se kratkotrajno blokira

- Ukoliko je korisnički nalog na 3. nivou i korisnik je menjao adresu:\
Nalog se prijavljuje adminu, kratkotrajno se blokira

- Ukoliko je korisnički nalog na 3. nivou i korisnik je otkazao
porudžbinu: Nalog se kratkotrajno blokira, notifikuje se admin

- Ukoliko je korisnički nalog na 2. nivou i korisnik je akumulativno
potrošio \>3000 dinara bez sumnjivih radnji:\
Nalog se postavlja na 1. nivo

- Ukoliko je korisnički nalog na 3. nivou i korisnik je akumulativno
potrošio \>5000 dinara bez sumnjivih radnji:\
Nalog se postavlja na 1. nivo

##  Primer Forward Chaining-a

1.  Korisnik kreira nov nalog i otkazuje prvu porudžbinu nakon njegovog
    kreiranja, sistem ga stavlja na 2. nivo, korisnik toga nije svestan

2.  Korisnik zatim opet otkazuje porudžbinu, sistem postavlja njegov
    nalog na 3. nivo i privremeno ga blokira

3.  Nakon prolaska privremene blokade, korisnik treci put
    zloupotrebljava sistem, sistem obavestava admina koji mu trajno
    blokira nalog, adresu i IP adresu

## CEP

CEP u ovom sistemu podrazumeva praćenje svih napravljenih i otkazanih
porudžbina kao i novokreiranih naloga na nivou grada.

- Registruju se sve dostave u celom gradu.
- Ukoliko se na istoj adresi kreira više od 2 naloga u roku od sat vremena
ili se otkaže više od 2 porudžbine u istom vremenskom roku, adresa se
automatski flaguje.

- Ukoliko se na istoj IP adresi kreiraju 2 naloga u toku jednog dana ili
otkažu više od 2 porudžbine, IP adresa se automatski flaguje.

- Ukoliko se na istoj IP adresi kreira nalog, poruči dostava u vrednosti
većoj od 7000 i otkaže se dostava, IP adresa se automatski flaguje.

## Backward chaining

-   Administrator sistema će imati uvid u sve razloge zbog kojeg je
    korisnik flagovan za trajno brisanje naloga

-   Administrator će takođe imati listu svih blokiranih adresa i IP
    adresa kao i razloga zbog kojih su blokirani kako bi mogao ručno da
    ih odblokira

-   Ukoliko korisnik odluči da se žali zbog toga što mu je nalog
    blokiran, sistem će sam proveriti iz kojih razloga je korisnik
    blokiran i ukoliko neki od razloga ne važi i dalje (na primer adresa
    više nije blokirana) sistem će odblokirati korisnika

-   Korisnička podrška će imadi uvid u stepen sumnjivosti i prethodne
    sumnjive radnje blokiranog korisnika na osnovu kojih će moći da
    prosledi zahtev za trajno blokiranje korisnika administratoru

## Template

Kako u zavisnosti od veličine grada, kao i aktuelnih cena u njemu,
određena pravila mogu biti previše blaga ili stroga. Administratori će
moći da podešavaju cenu potrebnu za spuštanje nivoa sumnjivosti,
dozvoljeni broj naloga na istoj adresi/IP adresi ( kao i vreme za koje
oni smeju da se prave), broj dozvoljenih promo kodova na novim nalozima
se takođe može podešavati na osnovu pravog broja aktuelnih promo kodova.
