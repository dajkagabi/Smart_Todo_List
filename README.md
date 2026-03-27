# Okos Teendők (Smart Todo List) - Wear OS

Ez egy modern, Jetpack Compose alapú teendőlista alkalmazás, amelyet kifejezetten **Wear OS** (Android okosóra) környezetre terveztek. Az alkalmazás segít a napi feladatok gyors rögzítésében és kezelésében, közvetlenül a csuklódról.

## 🚀 Funkciók

- **Tartós adattárolás**: A feladatok nem vesznek el az óra újraindításakor a beépített **Room adatbázisnak** köszönhetően.
- **Kategóriák**: Minden teendőhöz választható kategória (pl. Munka, Otthon, Bolt), így rendszerezettebb a lista.
- **Időbélyeg**: Az alkalmazás automatikusan menti és megjeleníti a feladat felvételének pontos idejét.
- **Kompakt bevitel**: Az órára optimalizált szövegbeviteli mező, amelynél az írás (billentyűzet/kézírás) az elsődleges.
- **Gyors törlés**: A modern Wear OS elvárásoknak megfelelően a teendők egy egyszerű oldalra húzással (**Swipe to Dismiss**) törölhetőek.
- **Szerkesztés**: Bármelyik meglévő teendőre kattintva az azonnal átnevezhető.

## 🛠 Technikai részletek

Az alkalmazás a legmodernebb Android technológiákat használja:
- **Kotlin**: Az alkalmazás elsődleges programozási nyelve.
- **Jetpack Compose (Wear Material 2)**: A deklaratív UI fejlesztéshez, speciálisan órára szabva (`ScalingLazyColumn`).
- **Room Database**: Helyi SQLite adatbázis az adatok biztonságos tárolásához.
- **MVVM Architektúra**: Tiszta kód és logikai elválasztás a `ViewModel` használatával.
- **StateFlow**: Reaktív adatfolyam, amely biztosítja, hogy a kijelző mindig az aktuális adatbázis-állapotot mutassa.
- **Splash Screen API**: Modern indítóképernyő a professzionális élményért.

## 📁 Projekt felépítése

- `data/`: Az adatbázis entitások, a DAO (adatkezelő parancsok) és az adatbázis konfiguráció helye.
- `presentation/`: A felhasználói felület (Compose fájlok) és a `ViewModel` helye.
- `theme/`: Az alkalmazás színei és stílusai.

---

