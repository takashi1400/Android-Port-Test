# Android-Port-Test
adb fowarding test

## メモ
1.
ターミナル上で
``` sh
adb forward tcp:9876 tcp:9876
```
を走らせてローカルホストのポート経由でアンドロイドのポートに転送する。

2. android側でアプリケーション開始

3. python側のコードを走らせる

