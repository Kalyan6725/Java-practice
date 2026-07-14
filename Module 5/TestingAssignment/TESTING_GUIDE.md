# Running Tests in IntelliJ IDEA - Solutions

## Solution 1: Rebuild Project (Recommended)
1. **File → Invalidate Caches** (or press `Ctrl+Shift+A` and type "Invalidate")
2. Select "Invalidate and Restart"
3. Wait for IntelliJ to restart and rebuild indices
4. Run tests again

## Solution 2: Clean and Rebuild
1. **Build → Clean Project**
2. **Build → Rebuild Project**
3. Run tests via IDE or Maven

## Solution 3: Use Maven from Terminal
```bash
# Run all tests via Maven (guaranteed to work)
cd c:\Users\samineni.kalyan\Desktop\TestingAssignment
mvn clean test

# Run specific test
mvn test -Dtest=MerchantServiceTest
```

## Solution 4: Run via Maven in IntelliJ
1. Open **Maven** panel on right side
2. Go to **TestingAssignment → Lifecycle → test**
3. Right-click and **Run Maven Goal**

## Solution 5: Fix IDE Classpath
1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. Go to **Modules → TestingAssignment**
3. Select **Sources** tab
4. Verify `src/test/java` is marked as **Test Sources**
5. Click **Apply** and **OK**
6. Rebuild project

## Why Tests Pass from CLI but Not IDE
- Maven always recompiles before testing
- IntelliJ uses its own compilation which may be out of sync
- Cache invalidation clears stale compiled classes

## Test Results
✅ **All 25 Tests Pass** when run via:
```bash
mvn clean test
```

**Output:**
```
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Quick Reference
| Command | Purpose |
|---------|---------|
| `mvn clean compile` | Compile code only |
| `mvn clean test` | Run all tests |
| `mvn test -Dtest=MerchantServiceTest` | Run one test class |
| `Ctrl+Shift+F10` (IDE) | Run test file (after rebuild) |
| `Ctrl+Shift+A` + "Invalidate" | Fix IDE caching issues |

## Recommended Approach
Use **Maven** from command line for reliable testing:
```bash
cd TestingAssignment
mvn clean test
```

This ensures:
- Clean build
- Fresh compilation
- Consistent results
- No IDE caching issues
