<div align="center">

<img src="fastlane/metadata/android/en-US/images/icon.png" alt="PipeTune app icon" width="200" />

# PipeTune

### YouTube Music client for Android — Metrolist フォーク / A fork of Metrolist

<br/>

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg?style=for-the-badge&labelColor=0d1117)](LICENSE)
[![Fork of Metrolist](https://img.shields.io/badge/fork%20of-Metrolist-181717?style=for-the-badge&labelColor=0d1117)](https://github.com/MetrolistGroup/Metrolist)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&labelColor=0d1117)](https://www.android.com)

<br/>

[**日本語**](#日本語) · [**English**](#english)

</div>

> [!IMPORTANT]
> PipeTune は [Metrolist](https://github.com/MetrolistGroup/Metrolist) の個人的なフォークです。上流プロジェクトとは独立して開発・動作します。
> PipeTune is a personal fork of [Metrolist](https://github.com/MetrolistGroup/Metrolist) and is developed independently from the upstream project.

> [!WARNING]
> **地域制限** — YouTube Music が利用できない地域では、対応地域に接続する **VPN またはプロキシ** が必要です。
> **Regional restriction** — If YouTube Music is unavailable in your region, a **VPN or proxy** connected to a supported region is required.

---

<div align="center">

### Screenshots / スクリーンショット

<img src="fastlane/metadata/android/en-US/images/screenshots/screenshot_1.png" alt="Home screen" width="30%" />
<img src="fastlane/metadata/android/en-US/images/screenshots/screenshot_2.png" alt="Artist screen" width="30%" />
<img src="fastlane/metadata/android/en-US/images/screenshots/screenshot_3.png" alt="Recognize music screen" width="30%" />
<img src="fastlane/metadata/android/en-US/images/screenshots/screenshot_4.png" alt="Listen together screen" width="30%" />
<img src="fastlane/metadata/android/en-US/images/screenshots/screenshot_5.png" alt="Player screen" width="30%" />
<img src="fastlane/metadata/android/en-US/images/screenshots/screenshot_6.png" alt="Player lyrics screen" width="30%" />

</div>

---

## 日本語

### 概要

PipeTune は YouTube Music クライアント **Metrolist** をベースにした個人フォークです。

- applicationId: `io.github.horihori2013.pipetune`
- ライセンス: [GPL-3.0](LICENSE)

### PipeTune での主な変更点

- **ライキッドガラス風 UI** — 同梱の AndroidLiquidGlass をベースに、ナビゲーションバーやトグルに透明感のあるガラス質感を適用
- **横画面・タブレット対応レイアウト** — 横画面では左レール＋半幅ミニプレイヤー、縦画面タブレットでは全幅ナビゲーションなど、画面モードごとに最適な配置
- **ミニプレイヤーとナビゲーションの位置調整** — 高さ・余白を統一し、隙間なくスムーズに連続する配置に
- **全言語でウクライナ支持メッセージに対応** — About ページのメッセージを 28 ロケールすべてで更新

### 機能

<table>
  <tr>
    <td width="50%" valign="top">

#### 再生
- YouTube Music の楽曲・動画をストリーミング
- バックグラウンド再生
- オフライン再生用のダウンロード・キャッシュ
- 無音区間のスキップ
- スリープタイマー

</td>
    <td width="50%" valign="top">

#### 音声
- 音量正規化
- テンポ・ピッチ制御
- イコライザー
- クロスフェード

</td>
  </tr>
  <tr>
    <td width="50%" valign="top">

#### 歌詞・発見
- ライブ同期歌詞
- AI による歌詞翻訳
- パーソナライズされたクイックピック
- 楽曲・アルバム・アーティスト・動画・プレイリストを検索

</td>
    <td width="50%" valign="top">

#### ライブラリ・アカウント
- ライブラリ管理
- ローカルプレイリスト
- プレイリストのインポート
- プレイリスト・キューの並べ替え
- YouTube Music アカウントにログイン
- お気に入り・アーティスト・アルバム・プレイリストの同期

</td>
  </tr>
  <tr>
    <td width="50%" valign="top">

#### ソーシャル
- フレンドとリアルタイムで同時再生（Listen Together）
- Last.fm スクローブリング
- Discord Rich Presence

</td>
    <td width="50%" valign="top">

#### インターフェース
- ホーム画面ウィジェット
- ライト / ダーク / ブラック / ダイナミックテーマ
- ダイナミックカラー ＋ 19 種のプリセットカラー
- Material 3
- ライキッドガラス風ナビゲーション（PipeTune）

</td>
  </tr>
</table>

### ビルド方法

要件: **JDK 21** / Android SDK

フレーバー: `foss`（デフォルト）/ `gms` / `izzy`

```bash
git clone https://github.com/horihori2013/PipeTune.git
cd PipeTune
./gradlew :app:assembleFossDebug
```

Windows の場合:

```powershell
.\gradlew.bat :app:assembleFossDebug
```

生成された APK:

```text
app/build/outputs/apk/universalFoss/debug/app-universal-foss-debug.apk
```

### クレジット

- 本プロジェクトは [Metrolist](https://github.com/MetrolistGroup/Metrolist)（作者: [Mo Agamy](https://github.com/mostafaalagamy)）のフォークです
- 上流コミュニティ: [Discord](https://dsc.gg/metrolist) / [Telegram](https://t.me/metrolistapp)
- 翻訳は上流の [Weblate](https://hosted.weblate.org/engage/metrolist/) から継承しています
- 主なインスピレーション: **[InnerTune](https://github.com/z-huang)** / **[OuterTune](https://github.com/DD3Boh)**
- ライブラリ & 連携: [Better Lyrics](https://better-lyrics.boidu.dev) · [metroserver](https://github.com/MetrolistGroup/metroserver) · [MusicRecognizer](https://github.com/aleksey-saenko/MusicRecognizer) · [zemer-cipher](https://github.com/ZemerTeam/zemer-cipher)

### 免責事項

本プロジェクトは YouTube、Google LLC、Metrolist Group LLC、またはそれらの関連会社・子会社とは **一切関係なく、資金提供・承認・推奨も受けていません**。

本プロジェクト内で言及されるすべての商標、サービスマーク、知的財産権はそれぞれの権利者に帰属します。

---

## English

### About

PipeTune is a personal fork of **[Metrolist](https://github.com/MetrolistGroup/Metrolist)**, a YouTube Music client for Android.

- applicationId: `io.github.horihori2013.pipetune`
- License: [GPL-3.0](LICENSE)

### What's changed in PipeTune

- **Liquid glass UI** — glass-styled navigation bar and toggles, powered by the bundled AndroidLiquidGlass library
- **Landscape & tablet layouts** — side rail with a half-width mini player in landscape, full-width bottom navigation on portrait tablets, and more
- **Mini player / navigation alignment** — unified heights and spacing for a seamless, gap-free layout
- **Ukraine message in every language** — the About page message has been updated across all 28 locales

### Features

<table>
  <tr>
    <td width="50%" valign="top">

#### Playback
- Stream any song or video from YouTube Music
- Background playback
- Download & cache for offline use
- Skip silence
- Sleep timer

</td>
    <td width="50%" valign="top">

#### Audio
- Audio normalization
- Tempo & pitch control
- Equalizer
- Crossfade

</td>
  </tr>
  <tr>
    <td width="50%" valign="top">

#### Lyrics & Discovery
- Live synced lyrics
- AI-powered lyrics translation
- Personalized quick picks
- Search songs, albums, artists, videos, and playlists

</td>
    <td width="50%" valign="top">

#### Library & Account
- Full library management
- Local playlists
- Import playlists
- Reorder songs in playlist or queue
- YouTube Music account login
- Sync songs, artists, albums, and playlists

</td>
  </tr>
  <tr>
    <td width="50%" valign="top">

#### Social
- Listen together with friends in real-time
- Last.fm integration for scrobbling
- Safe Discord Rich Presence

</td>
    <td width="50%" valign="top">

#### Interface
- Home screen widget
- Light / Dark / Black / Dynamic theme modes
- Dynamic color + 19 preset color palettes
- Built with Material 3
- Liquid glass navigation (PipeTune)

</td>
  </tr>
</table>

### Building

Requirements: **JDK 21** / Android SDK

Flavors: `foss` (default) / `gms` / `izzy`

```bash
git clone https://github.com/horihori2013/PipeTune.git
cd PipeTune
./gradlew :app:assembleFossDebug
```

On Windows:

```powershell
.\gradlew.bat :app:assembleFossDebug
```

Output APK:

```text
app/build/outputs/apk/universalFoss/debug/app-universal-foss-debug.apk
```

### Credits

- This project is a fork of [Metrolist](https://github.com/MetrolistGroup/Metrolist) by [Mo Agamy](https://github.com/mostafaalagamy)
- Upstream community: [Discord](https://dsc.gg/metrolist) / [Telegram](https://t.me/metrolistapp)
- Translations are inherited from upstream [Weblate](https://hosted.weblate.org/engage/metrolist/)
- Main inspirations: **[InnerTune](https://github.com/z-huang)** / **[OuterTune](https://github.com/DD3Boh)**
- Libraries & integrations: [Better Lyrics](https://better-lyrics.boidu.dev) · [metroserver](https://github.com/MetrolistGroup/metroserver) · [MusicRecognizer](https://github.com/aleksey-saenko/MusicRecognizer) · [zemer-cipher](https://github.com/ZemerTeam/zemer-cipher)

### Disclaimer

This project is **not affiliated with, funded, authorized, endorsed by, or in any way associated** with YouTube, Google LLC, Metrolist Group LLC, or any of their affiliates and subsidiaries.

All trademarks, service marks, and intellectual property rights referenced in this project belong to their respective owners.

---

## Third-party licenses / サードパーティーライセンス

| Project / プロジェクト | License / ライセンス | Usage / 使用箇所 |
|---|---|---|
| **[PipeTune](https://github.com/horihori2013/PipeTune)** (fork of Metrolist) | [GPL-3.0](LICENSE) | 本アプリ / this app |
| **[AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass)** (Kyant0) — including its Backdrop module | [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) | ガラス風ナビゲーション・トグルの UI / glass-style navigation & toggle UI. Backdrop のソースは `app/src/main/kotlin/com/kyant/backdrop/` に同梱（`LICENSE` ファイル付き） / Backdrop sources are vendored at `app/src/main/kotlin/com/kyant/backdrop/` with the `LICENSE` file included |
| **[Shapes](https://github.com/Kyant0/Shapes)** — `io.github.kyant0:shapes` | [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) | Gradle 依存 / Gradle dependency |
| **[wireproxy-awg](https://github.com/wgtunnel/wireproxy-awg)** — `libwireproxy.so` | [ISC](https://github.com/wgtunnel/wireproxy-awg/blob/master/LICENSE) | WireGuard ユーザー空間プロキシ（SOCKS5）のネイティブライブラリ。`app/src/main/jniLibs/arm64-v8a/libwireproxy.so` に同梱（`*.so` は gitignore 対象のためリポジトリには未収録、ビルド時に APK へバンドル）。[amneziawg-go](https://github.com/amnezia-vpn/amneziawg-go)（MIT）と [go-socks5](https://github.com/things-go/go-socks5)（MIT）を静的リンク / native userspace WireGuard SOCKS5 proxy bundled at `app/src/main/jniLibs/arm64-v8a/libwireproxy.so` (gitignored, packaged into the APK); statically links amneziawg-go (MIT) and go-socks5 (MIT) |

WireGuard 関連の Kotlin 実装（`WireGuardConfig.kt` / `WireGuardManager.kt` / `WireGuardNative.kt` / `WireGuardSettings.kt`）は本プロジェクト独自のコードで、[GPL-3.0](LICENSE) の下で提供されます。
The WireGuard-related Kotlin sources are original work released under GPL-3.0.

> WireGuard® は Jason A. Donenfeld の登録商標です。/ WireGuard is a registered trademark of Jason A. Donenfeld.

---

<div align="center">

<br/>

**This project stands with Ukraine 🇺🇦**

<br/>

PipeTune is a fork of [Metrolist](https://github.com/MetrolistGroup/Metrolist) · Original project by [Mo Agamy](https://github.com/mostafaalagamy)

</div>
