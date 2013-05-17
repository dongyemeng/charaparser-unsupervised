charaparser-unsupervised
========================

Java implementation of unsupervised CharaParser code

* Read Treatment

** Sentence segmentation
*** Handle abbreviation. 
First replace all dot marks after abbreviation by "[DOT]". After sentence segmentation, restore them.

The regular expression like "blv?d" would match both "blvd" and "bld". The question mark makes the previous letter optional.

* Manage Data Holder
** Update Table
***Discount POS
    Given a word, its old POS, its new POS, and the mode,
    1. Find the flag of the word in Unknownword holder, then select all words from Unknownword table who have the same flag including itself
    1. For each of them, 
        1.1. If from WordPOS holder its certaintyU is less than 1, AND mode is "all"
		    1.1.1. Delete the entry from WordPOS holder
		    1.1.1. Update Unknownword holder
		    1.1.1. If the POS is "s" or "p", delete all entries contains word from Singularplural holder as well
        1.1. Else insert (nword, Oldpos, newpos) into discounted table
