reg add "HKEY_CURRENT_USER\Environment" /v PRISM_AGENCY /t REG_EXPAND_SZ /d MDH
reg add "HKEY_CURRENT_USER\Environment" /v PRISM_BUSINESS_RULES /t REG_EXPAND_SZ /d %PRISM_DATA%\prism_rules\BusinessRules
reg add "HKEY_CURRENT_USER\Environment" /v PRISM_DATA /t REG_EXPAND_SZ /d c:\prism_data
reg add "HKEY_CURRENT_USER\Environment" /v PRISM_LUCENE /t REG_EXPAND_SZ /d %PRISM_DATA%\prism_lucene
reg add "HKEY_CURRENT_USER\Environment" /v PRISM_REPORTS /t REG_EXPAND_SZ /d %PRISM_DATA%\prism_reports