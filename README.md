# Docker setup
## Windows instalacija
Downloadovati [odavde](https://docs.docker.com/desktop/windows/install/). 
Za windows je potrebno imati wsl2 i enable-ovati Hyper-V. 
### WSL2
Nakon instalacije i restarta ce docker da zatrazi da instalirate wsl2 ukoliko ga vec nemate. Potrebno je otici na link koji docker da i uraditi step 4 i step 5. Step 6 nije potrebno raditi.
### Hyper-V
Mislim da treba da se enable-uje u BIOS-u, ali verovatno docker daje neka uputstva kako da se izvede.
## MacOS instalacija
Downloadovati [odavde](https://docs.docker.com/desktop/mac/install/) ili sa brew-om uz komandu:
```
brew install --cask docker
```
## Pokretanje
Neophodno je imati docker aplikaciju pokrenutu.
U konzoli (za Windows to je PowerShell ili Bash(dolazi uz git), za MacOS standardni terminal, a uvek mozete i po svojoj preferenci), otvorite folder sa docker-compose.yml i pokrenite komandu:
```
docker-compose up -d
```
Proces startovanja mozete pratiti u Docker app-u. Ovo pokrece sve servise trenutno setupovane da rade sa dockerom.
### ARM procesori
Ukoliko neko radi na ARM procesoru, image za mysql se mora pokrenuti u compatability mode-u. Koristite ovu komandu:
```
docker-compose -f docker-compose_arm.yml up -d

```
## Zaustavljanje
Komanda je:
```
docker-compose stop
```
## Resursi
Docker zna da koristi dosta resursa, i moja preporuka je da ukoliko vidite da vam racunar sporije radi dok je docker ukljucen, modifikujete kolicinu resursa koju docker zahteva.
# Baza
Baza radi na portu 3307 na localhostu, ime baze je osiguranje, konektujete se na root korisnika, a password mozete naci u .env file-u.
# Eureka
Eureka radi na portu 8761.