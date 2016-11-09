package algorithms.miller_rabin;

/**
 * 
 * @author moqiguzhu
 * @date 2016-04-03
 * @version 1.0
 */
import java.util.Scanner;

public class MillerRabin {
  // 前提条件是a和p互质且a<p
  public static boolean fermatTest(long a, long p) {
    return mod_exp(a, p - 1, p) == 1;
  }

  // 位操作计算 a*b%n 不要直接使用乘法做，很容易爆掉
  static long mod_mul(long a, long b, long n) {
    long res = 0;
    while (b != 0) {
      if ((b & 1) != 0)
        res = (res + a) % n;
      a = (a + a) % n;
      b >>= 1;
    }
    return res;
  }

  // 位操作计算a^b%n
  static long mod_exp(long a, long b, long n) {
    long res = 1;
    while (b != 0) {
      if ((b & 1) != 0)
        res = mod_mul(res, a, n);
      a = mod_mul(a, a, n);
      b >>= 1;
    }
    return res;
  }
  
  // 二分法  log(n)的时间复杂度
  public static long mod_exp_simple(long a, long b, long n) {
    if(b == 1)
      return a % n;
    long tt = mod_exp_simple(a, b/2, n);
    if(b % 2 == 0) {
      return mod_mul(tt, tt, n);
    } else {
      return mod_mul(a,mod_mul(tt, tt, n),n);
    }
  }


  public static boolean naive(long p) {
    for (int i = 2; i < Math.sqrt(p) + 1; i++) {
      if (p % i == 0) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isCarmichael(long p) {
    boolean flag = false;
    for(long j = 2; j < p && p % j != 0; j++) {
      flag = fermatTest(j, p);
      if(!flag) break;
    }
    if(flag && !naive(p)) {
      return true;
    } else {
      return false;
    }
  }


  public static boolean miller_rabin(long p) {
    boolean flag = (p == 1) || (p != 2 && (p % 2) == 0) || (p != 3 && (p % 3) == 0)
        || (p != 5 && (p % 5) == 0) || (p != 7 && (p % 7) == 0);
    if (flag) {
      return false;
    }

    if (p < 100) {
      return naive(p);
    }

    long[] testFactors = new long[] {2, 3, 5, 7, 9};
    for (int i = 0; i < testFactors.length; i++) {
      if (fermatTest(testFactors[i], p)) {
        long t = p - 1;
        while (t % 2 == 0) {
          t = t >> 1;
          long tt = mod_exp_simple(testFactors[i], t, p);
          if (tt == p - 1) {
            break;
          }
          if (tt != 1) {
            return false;
          }
        }
      } else {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int t = sc.nextInt();
    int i = 0;
    StringBuilder res = new StringBuilder();
    while (i++ < t) {
      long x = sc.nextLong();
      if (MillerRabin.miller_rabin(x)) {
        res.append("Yes\n");
      } else {
        res.append("No\n");
      }
    }
    System.out.println(res.toString());
    sc.close();
//    System.out.println(MillerRabin.isCarmichael(561));
  }
}

