apt_repository 'rabbitmq' do
  uri 'http://www.rabbitmq.com/debian/'
  distribution 'testing'
  components ['main']  
  deb_src true
  key rabbitmq-signing-key-public.asc
  action :add
end

package 'rabbitmq-server'
