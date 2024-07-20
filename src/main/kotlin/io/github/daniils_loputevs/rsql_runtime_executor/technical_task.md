Возможность выполнить RSQL query над List<Entity> как filter или sort операции

## Фичи:
### Поддержка коллекций:
- List<Entity>
- MutableList<Entity>
- Sequence<Entity>
- Stream<Entity>

### Поиск значение по селектору среди
- поиск вложенных полей
- поиск вложенных полей - support Array & Map syntax
- (reflection) Entity class
- (reflection) Entity interfaces class property (cascade)
- (reflection) Entity all super classes hierarchy property (cascade)
- (custom) extension property
- (Kotlin) extension property
- (reflection) Entity all super classes hierarchy property (cascade)

### Поддержка операторов
- all standard RSQL operators
- custom operators (?NoArg, SingeArgument, MultiArguments)
- (reflection) Kotlin operators override

### Exceptions, Debug,  Logging(?Slf4j)
### Docs
### Test & Examples
### 