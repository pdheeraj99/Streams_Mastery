# 🌊 JAVA STREAMS – CHEAT SHEET (Creative Edition)

```
🧩 MENTAL MODEL:
🌱 Source  →  🎛️ Configure  →  🧰 Intermediate  →  🏁 Terminal (ON switch!)
```

---

## 🚫 GOLD RULES (Malli Malli Remember!)

| Rule | Telugu | English |
|------|--------|---------|
| 🧨 One-shot | "Oka sari use chesav, inka vaddu!" | Stream reuse = Exception |
| 🧊 Non-interference | "Pipeline run avthunde, source ni touch cheyyaku!" | Don't modify source |
| 🧘 Stateless | "Baita variables use cheyyaku lambda lo!" | Avoid shared mutable state |

---

## 🎛️ CONFIGURATION (Pipeline Behavior)

| Op | Telugu Vibe | What it does |
|----|-------------|--------------|
| `parallel()` 🚀 | "Race lo patukelli!" | Multiple threads use chestadi |
| `sequential()` 🐢 | "Oka oka ga, patience ga!" | Single thread, ordered |
| `unordered()` 🎲 | "Order pattinchukoku, speed kavali!" | Faster parallel processing |

**Memory Trick: "PSIU"** → Parallel, Sequential, IsParallel, Unordered

---

## 🔎 FILTERING - "Nuvvu Kavala? Vadda?"

### filter(Predicate) ✅

```
🎭 "Nuvvu naku KAVALI" 🥳 → true → STAYS
🎭 "Nuvvu naku VADDU" 😤 → false → GOES
```

```java
.filter(n -> n > 5)  // 5 kanna pedda? Kavali! Else vaddu!
```

### takeWhile(Predicate) 🧠⚡

```
🏃 "Nuvvu nachav, nuvvu nachav... OH! Nuvvu nachaledhu? STOP! Inka evaru vaddu!"
```

```java
.takeWhile(n -> n < 10)  // 10 kanna takkuva varaku teesuko, first fail → STOP
```

### dropWhile(Predicate) 🧠

```
🗑️ "Nuvvu nachaledhu, velipu! Nuvvu nachaledhu, velipu!... OH nacchav? 
    Ippudu nundi andaru STAY!"
```

```java
.dropWhile(n -> n < 5)  // 5 kanna takkuva varaku skip, first fail → keep rest
```

---

## 🔁 TRANSFORM - "Makeover Time!"

### map(Function) ✅

```
💇 "Oka person → Oka makeover"
   Ram → RAM (uppercase)
   1 → 2 (doubled)
```

```java
.map(name -> name.toUpperCase())  // 1 input → 1 output
```

**Memory Trick:** "1→1 transform, count same untadi"

### flatMap(Function) ✅

```
📦 "Oka box open chesthe CHALA items vasthayi!"
   ["Ram", "Sita"] → ['R','a','m','S','i','t','a']
   [[1,2], [3,4]] → [1, 2, 3, 4]
```

```java
.flatMap(list -> list.stream())  // 1 input → MANY outputs (flattened)
```

**Memory Trick:** "1→Many, flatten chestadi, box open!"

### mapMulti(BiConsumer) ✅

```
🎁 "Same as flatMap, but no extra Stream create avvadu - PERFORMANCE!"
```

**Memory Trick:** "flatMap but no temporary streams"

---

## 🔢 PRIMITIVE CONVERSION - "Object → Number"

### mapToInt / mapToLong / mapToDouble ✅

```
📊 "String/Object stream nundi NUMBER stream kavali"
   Employee → salary (int)
   Product → price (double)
```

```java
.mapToDouble(Product::getPrice)  // Stream<Product> → DoubleStream
.sum()  // Ippudu sum() vadachu!
```

**Memory Trick:** "Object → Primitive, ippudu sum/average directly!"

---

## 🧠 STATEFUL OPS - "Vallu REMEMBER chestaru!"

### distinct() 🧠

```
🧓 "Eedu already vacchadu? Rendu sari vaddu!"
   [1,2,2,3,3,3] → [1,2,3]
```

**Memory Trick:** "Seen elements ni remember chestadi (Set lantidi)"

### sorted() 🧠

```
📚 "Andarnni buffer lo petti, THEN arrange!"
   [3,1,2] → wait... wait... → [1,2,3]
```

**Memory Trick:** "First BUFFER all, then SORT - costly!"

### limit(n) 🧠⚡

```
✋ "First N mandi matrame! Migilina vallu GO BACK!"
   [1,2,3,4,5].limit(3) → [1,2,3]
```

**Memory Trick:** "First N, short-circuits!"

### skip(n) 🧠

```
⏭️ "First N mandi SKIP! Migatha vallu randi!"
   [1,2,3,4,5].skip(2) → [3,4,5]
```

**Memory Trick:** "Drop first N"

---

## 🧪 DEBUG - "X-Ray Vision"

### peek(Consumer) ⚠️

```
👀 "Pipeline lo emi jarguthundo CHOODADANIKI - but production lo vadaku!"
```

```java
.peek(x -> System.out.println("Now: " + x))
```

**Warning:** Short-circuit optimizations valla anni elements ki run avvakapovachu!

---

## 🏁 TERMINAL OPS - "ON SWITCH!" ⚡

| Category | Operations | Telugu Vibe |
|----------|------------|-------------|
| 📦 Collecting | `collect()`, `toList()`, `toArray()` | "Anni gather cheyyi!" |
| 🔢 Counting | `count()` | "Entha mandi unnaru?" |
| ➕ Reducing | `reduce()`, `sum()`, `average()` | "Andarnni COMBINE cheyyi!" |
| ✅ Matching | `anyMatch()`, `allMatch()`, `noneMatch()` ⚡ | "Evadaina/Andaru/Evadu match?" |
| 🎯 Finding | `findFirst()`, `findAny()` ⚡ | "First/Any odu vadu ikkada?" |
| 🚶 Doing | `forEach()`, `forEachOrdered()` | "Oka oka ki action cheyyi!" |
| 📊 Stats | `min()`, `max()`, `summaryStatistics()` | "Smallest? Biggest? Everything?" |

**Memory Trick:** "CCRR MMFF" → Collect/Count, Reduce, Match, Find, ForEach

---

## ⚡ SHORT-CIRCUIT Heroes (Early Exit!)

```
These DON'T process ALL elements - efficiency kings!

🎯 findFirst(), findAny() → "First match dorikindha? DONE!"
✅ anyMatch()             → "Oka true dorikindha? DONE!"
❌ allMatch()             → "Oka false dorikindha? DONE!"
🚫 noneMatch()            → "Oka true dorikindha? DONE (false)!"
✋ limit(n)               → "N items ayyindha? DONE!"
🏃 takeWhile()            → "First fail? DONE!"
```

---

## 🎪 COLLECTORS (collect() tho use chestam)

| Collector | Telugu | Use |
|-----------|--------|-----|
| `toList()` | "List lo pettuko" | Default |
| `toSet()` | "Unique ga pettuko" | Remove duplicates |
| `toMap()` | "Key-Value pair cheyyi" | Lookup map |
| `joining()` | "Anni strings ni GLUE cheyyi" | Concatenate |
| `groupingBy()` | "Category wise DIVIDE cheyyi" | Group |
| `partitioningBy()` | "Pass/Fail laga TWO groups" | Binary split |
| `teeing()` | "TWO collectors oka sari run!" | Dual collect |

---

## 🧠 FINAL MEMORY TRICKS

```
📌 filter   → "Kavala? Vadda?"
📌 map      → "1→1 makeover"
📌 flatMap  → "1→Many, box open!"
📌 sorted   → "Buffer → Sort"
📌 distinct → "Already chusa? Skip!"
📌 limit/skip → "First N / Drop N"
📌 takeWhile → "Take UNTIL fail"
📌 dropWhile → "Drop UNTIL fail"
📌 Terminal → "ON switch!"
```

---

**🎯 Interview lo remember: "Stream = LAZY pipeline, Terminal = ON button!"** 💪
