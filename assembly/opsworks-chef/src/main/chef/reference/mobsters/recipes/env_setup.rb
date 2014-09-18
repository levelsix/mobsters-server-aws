template '/etc/profile.d/lvl6envvars.sh' do
  source 'lvl6envvars.erb'
end

cron 'ntpdate' do
  minute '1'
  hour '*'
  day '*'
  month '*'
  weekday '*'
  command '/usr/sbin/ntpdate-debian >/dev/null 2>&1'
end
