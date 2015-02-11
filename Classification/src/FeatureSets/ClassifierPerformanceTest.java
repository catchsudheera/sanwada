package FeatureSets;

import java.io.File;
import java.lang.management.ManagementFactory;
// import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.management.RuntimeMXBean;
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.Random;



public class ClassifierPerformanceTest
{


    public static void printUsage(Runtime runtime)
    {
        long total, free, used;
        int mb = 1024*1024;

        total = runtime.totalMemory();
        free = runtime.freeMemory();
        used = total - free;
        System.out.println("\nTotal Memory: " + total / mb + "MB");
        System.out.println(" Memory Used: " + used / mb + "MB");
        System.out.println(" Memory Free: " + free / mb + "MB");
        System.out.println("Percent Used: " + ((double)used/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%");
    }
    public static void log(Object message)
    {
        System.out.println(message);
    }

    public static int calcCPU(long cpuStartTime, long elapsedStartTime, int cpuCount)
    {
        long end = System.nanoTime();
        long totalAvailCPUTime = cpuCount * (end-elapsedStartTime);
        long totalUsedCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStartTime;
        //log("Total CPU Time:" + totalUsedCPUTime + " ns.");
        //log("Total Avail CPU Time:" + totalAvailCPUTime + " ns.");
        float per = ((float)totalUsedCPUTime*100)/(float)totalAvailCPUTime;
        log( per);
        return (int)per;
    }

    static boolean isPrime(int n)
    {
        // 2 is the smallest prime
        if (n <= 2)
        {
            return n == 2;
        }
        // even numbers other than 2 are not prime
        if (n % 2 == 0)
        {
            return false;
        }
        // check odd divisors from 3
        // to the square root of n
        for (int i = 3, end = (int)Math.sqrt(n); i <= end; i += 2)
        {
            if (n % i == 0)
            {
                return false;
            }
        }
        return true;
    }

    public static void main(String [] args)
    {
        int mb = 1024*1024;
        int gb = 1024*1024*1024;
             /* PHYSICAL MEMORY USAGE */
        System.out.println("\n**** Sizes in Mega Bytes ****\n");
        com.sun.management.OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        //RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
                java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        long physicalMemorySize = os.getTotalPhysicalMemorySize();
        System.out.println("PHYSICAL MEMORY DETAILS \n");
        System.out.println("total physical memory : " + physicalMemorySize / mb + "MB ");
        long physicalfreeMemorySize = os.getFreePhysicalMemorySize();
        System.out.println("total free physical memory : " + physicalfreeMemorySize / mb + "MB");
            /* DISC SPACE DETAILS */
        File diskPartition = new File("C:");
        File diskPartition1 = new File("D:");
        File diskPartition2 = new File("E:");
        long totalCapacity = diskPartition.getTotalSpace() / gb;
        long totalCapacity1 = diskPartition1.getTotalSpace() / gb;
        double freePartitionSpace = diskPartition.getFreeSpace() / gb;
        double freePartitionSpace1 = diskPartition1.getFreeSpace() / gb;
        double freePartitionSpace2 = diskPartition2.getFreeSpace() / gb;
        double usablePatitionSpace = diskPartition.getUsableSpace() / gb;
        System.out.println("\n**** Sizes in Giga Bytes ****\n");
        System.out.println("DISC SPACE DETAILS \n");
        //System.out.println("Total C partition size : " + totalCapacity + "GB");
        //System.out.println("Usable Space : " + usablePatitionSpace + "GB");
        System.out.println("Free Space in drive C: : " + freePartitionSpace + "GB");
        System.out.println("Free Space in drive D:  : " + freePartitionSpace1 + "GB");
        System.out.println("Free Space in drive E: " + freePartitionSpace2 + "GB");
        if(freePartitionSpace <= totalCapacity%10 || freePartitionSpace1 <= totalCapacity1%10)
        {
            System.out.println(" !!!alert!!!!");
        }
        else
            System.out.println("no alert");

        Runtime runtime;
        byte[] bytes;
        System.out.println("\n \n**MEMORY DETAILS  ** \n");
        // Print initial memory usage.
        runtime = Runtime.getRuntime();
        printUsage(runtime);

        // Allocate a 1 Megabyte and print memory usage
        bytes = new byte[1024*1024];
        printUsage(runtime);

        bytes = null;
        // Invoke garbage collector to reclaim the allocated memory.
        runtime.gc();

        // Wait 5 seconds to give garbage collector a chance to run
        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            e.printStackTrace();
            return;
        }

        // Total memory will probably be the same as the second printUsage call,
        // but the free memory should be about 1 Megabyte larger if garbage
        // collection kicked in.
        printUsage(runtime);
        for(int i = 0; i < 30; i++)
        {
            long start = System.nanoTime();
            // log(start);
            //number of available processors;
            int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
            Random random = new Random(start);
            int seed = Math.abs(random.nextInt());
            log("\n \n CPU USAGE DETAILS \n\n");
            log("Starting Test with " + cpuCount + " CPUs and random number:" + seed);
            int primes = 10000;
            //
            long startCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            start = System.nanoTime();
            while(primes != 0)
            {
                if(isPrime(seed))
                {
                    primes--;
                }
                seed++;

            }
            float cpuPercent = calcCPU(startCPUTime, start, cpuCount);
            log("CPU USAGE : " + cpuPercent + " % ");


            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {}
        }

        try
        {
            Thread.sleep(500);
        }//`enter code here`
        catch (Exception ignored) { }
    }

    /*public static void main(String [] args) throws IOException {
        String x="හොයන්න, හැරෙනවා, ඉන්නකෝ, යොදාගන්න, නඟිනවා, නමපන්, ජීවත්වෙන්න, එනවා, උගන්නන්න, ඉඩතියන්න, අදින්න, දියල්ලා, ගන්න, දකින්න, දෙන්න, බැහැපං, හිතන්, ඇවිදින්න, හිටහන්, කරන්න පටංගත්තොත්, නැවතියන්, නැඟිටින්න, ඉන්න, දීපන්, එන්න, නැගිටිනවා, කියපං, වරෙල්ලා, පලයං, එහෙනම් ඔයා මාව අස්කරලා දාන්න, දුවපන්, පැදපන්, සංතෝෂවෙයල්ලා, කතාබහකරන්න, ඇඳගන්න, ගොඩඑන්න, දුවපල්ලා, අල්ලන්න, පෙරළන්න, ඇරපන්, බලාගන්න, එවන්න, නවත්වනවා, දීපල්ලා, ගේන්න, බලමු, නගින්න, බහින්න, අස්කරගන්න, කරනවා, දෙනවා, නවත්තන්න, දුක්වෙන්න, යන්නකෝ, මරන්න, යමල්ලා, යවන්න, ඔබන්න, හිටගන්නවා, කියන්නකෝ, තේරුම්ගන්න, කාපන්, වහගන්නවා, අරගන්න, වෙනවා, යමන්, එන්න ඇතුලට, කරන්න, වරෙන්,, තියන්න, නිදාගන්න, ඉන්නවා, ගේ්න්න, ගැටගහන්න, කතාකරන්න, කියපන්, කරගන්න,ලිහන්න, මනින්න, වෙන්න, රවට්ටන්න, අඬන්න, හිටපං, දාපන්, අහන්න, හිනාවෙන්න, අරිනවා, වාඩිවෙන්න, උස්සන්න, දුවන්න, හරිගස්සන්න, ගහගන්න, වෙනවා,වදවෙන්න, අරින්න, නගිනවා, නැගිට්ටවන්න, පලයල්ලා, ගෙනියන්න, වෙයල්ලා, නිදාගනින්, වාඩිවෙන්න,වෙන්න, වක්කරපන්, බේරගන්න, වරෙන්, දියන්, වදවෙන්න, බයවෙන්න, පලයන්, පුහුණුවෙන්න, ඉවසන්න, ගහන්න, ඉදපන්, හිටපන්, නැඟිටපල්ලා, දාන්න, හිටපංකෝ, බලන්න, කන්න, ගනින්, ඉඩදෙන්න, පෙන්වන්න, කරහන්, අහන්නකෝ, මැරියන්, තියාගන්න, පටන්ගන්න, දාගන්න, උඩින් තියන්න, කියන්න, යන්න, නවතින්න, නඟින්න, එකතුවෙන්න, කරපන්, එන්නකෝ, යනවා, අතදාන්න, කරගන්න, අල්ලගන්න, වෙයන්, හිතන්න, විවේකගනින්, වරෙව්, නැඟිටපන්, කඩන්න";
        System.out.print("\"");
        for(int i=0;i<x.length();i++){
            if(x.charAt(i)==','){
                System.out.print("\",");
            }
            else if(x.charAt(i)==' '){
                System.out.print(" \"");
            }
            else{
                System.out.print(x.charAt(i));
            }
        }
        System.out.println("\"");
    }*/
}