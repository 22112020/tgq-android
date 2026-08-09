# TGQ Android — Aplikasi Native Luna Core

Aplikasi Android **native** (Kotlin + Jetpack Compose) untuk TGQ — menggantikan wrapper WebView lama.
Mengikuti design system `design-preview/` (dark premium, gradient violet→magenta→gold).

## Fitur
- **Splash** → **Login admin** (token disimpan lokal, `Bearer` ke API TGQ)
- **Beranda**: ringkasan Prediksi Hoki + akses cepat + pasaran terkini
- **Pasaran**: daftar semua pasaran + pencarian, detail + prediksi per pasaran
- **Hoki**: prediksi 5D utama, backup, statistik analisis, top-10 digit
- **Input**: **Luna Parse** (port dari `UI/index.html`) — tempel hasil → parse → kirim ke `/api/input`
- **Profil**: ganti server base URL, uji koneksi, logout

## Struktur
- `android/` — Project Gradle Android (Kotlin + Compose)
- `design-preview/` — Desain preview (HTML + PNG)
- `releases/` — APK hasil build
- `.github/workflows/build-apk.yml` — Workflow build otomatis (debug + release unsigned)

## Build
APK di-build otomatis oleh GitHub Actions setiap push ke `main` (perubahan di `android/`).
Artifact `tgq-apk` berisi `debug/app-debug.apk` (bisa langsung install) dan `release/app-release-unsigned.apk`.

Build manual (butuh Android SDK + Java 17):
```bash
cd android
gradle :app:assembleDebug
```

## API
Klien terhubung ke TGQ API (`ApiClient.kt`, OkHttp + kotlinx-serialization):
`/api/hoki`, `/api/markets`, `/api/engines`, `/api/predict`, `/api/login`, `/api/input`.
Default server: `https://tgq.duaduasatusatu.qzz.io` (bisa diubah di Profil).
