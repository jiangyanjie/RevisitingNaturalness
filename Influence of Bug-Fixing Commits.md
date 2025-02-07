As suggested by the preceding sections, one of our key findings (i.e., fixing bugs cannot significantly improve the naturalness of source code) is inconsistent with the findings reported by Ray et al.[51]. One possible reason for the inconsistency is that our evaluation excludes bug-irrelevant changes in bug-fixing commits whereas Ray et al. take all changes in bug-fixing commits as patches for software defects. To validate this potential reason, in this section, we reuse Ray's setting (i.e., taking the whole commits as patches for defects) and repeat the evaluation in Section 4 and Section 6.

**Process**

Following Ray et al.[51], in this section we investigate how bug-fixing commits influence the naturalness of source code lines. To this end, we compute the naturalness of code lines (in the buggy version) removed/modified by bug-fixing commits, code lines (in the fixed version) added/updated by bug fixing commits, and code lines (in both buggy version and fixed version) that have not been influenced by the bug-fixing commits. For convenience, we call such three categories of code lines as Potential Buggy Lines, Potential Fixed Lines, and Bug-Free Lines, respectively. Keyword "potential" in "Potential Buggy Lines" suggests that code lines removed/modified by bug-fixing commits are not necessarily buggy. The same is true for the key word "potential" in "Potential Fixed Lines". By comparing the naturalness of Potential Buggy Lines, Potential Fixed Lines, and Bug-Free Lines, we may reveal how bug-fixing commits influence the naturalness of code lines. By comparing the influence of bug-fixing commits against the influence of bug-fixing changes (as presented  in Section 4.1 and Section 6.1), we may also reveal the importance of distinguishing bug-irrelevant changes (in bug-fixing commits) from bug-fixing changes.  

**Results and Analysis**

![](https://github.com/jiangyanjie/RevisitingNaturalness/blob/main/Fig/table8.png)
![](https://github.com/jiangyanjie/RevisitingNaturalness/blob/main/Fig/fig9.png)
![](https://github.com/jiangyanjie/RevisitingNaturalness/blob/main/Fig/Fig10.png)

The results are presented in Table 8, Figure 9, and Figure 10. From these table and figures, we make the following observations.

- Potential Buggy lines are less natural, i.e., with higher entropy than bug-free ones. On Defects4J, the average entropy of buggy lines is 0.22, substantially higher than that (0.00169) of bug-free lines. On GrowingBugs, the average line entropy of buggy lines is 0.24, substantially higher than that (0.00238) of bug-free lines, too.
- Bug-fixing commits have a mixed influence on the naturalness of code lines. On one side, on Defects4J, bug-fixing commits improve the naturalness of source code lines from 0.22 (potential buggy lines) to 0.19 (potential fixed lines). Wilcoxon non-parametric tests (p-value=0.0237 < 0.05) and Cohen’s D effect size ( Cohen’s Size = 0.0189) suggest that the difference is statistically significant. On the other side, on GrowingBugs, the average entropy of potential fixed lines (0.25) is even higher than that of potential buggy lines (0.24)

By comparing the observations in this section and the findings reported by Ray et al. [51], we observe that they are consistent in one bug repository (Defects4J): (Potential) buggy lines are less natural and bug-fixing commits can significantly improve the naturalness of involved code lines. However, on another bug repository (GrowingBugs), some observations in this section (i.e., bug-fixing commits can significantly improve the naturalness of involved code lines) is still inconsistent with the findings reported by Ray et al. [51].

By comparing the observations in this section and observations in Section 4 and Section 4, we conclude that excluding bug-irrelevant changes from bug-fixing commits can substantially influence the observations and conclusions. Consequently, the exclusion is valuable and indispensable.
