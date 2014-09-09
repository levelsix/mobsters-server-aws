include_recipe 'jenkins'

jenkins_password_credentials 'ashwin' do
  description 'Ashwin Kamath'
  password 'ashwin'
end

jenkins_password_credentials 'kelly' do
  description 'Kelly Roman'
  password 'kelly'
end

jenkins_password_credentials 'jheinnic' do
  description 'John Heinnickel'
  password 'jheinnic'
end

jenkins_password_credentials 'admin' do
  description 'Silly Rabbit'
  password 'admin'
end

jenkins_private_key_credentials 'keytest' do
  description 'RSA Credentials Test'
  private_key "-----BEGIN RSA PRIVATE KEY-----\nMIIEpQIBAAKCAQEApxNQylUqi3J+ybGdhS0NZbccA+WgAow9Xk25AoMDa01JdufULe78Fj038LOF\newFL5WONGmkP6QopvHlocAjSwWo1LuqemQ7uv9Gq8Qz7bxqU2+dOYRkRgxh0KqcfKwdqgtlG/0t1\n3Ob6Il0AXdwu+kC5dhXI9vfcLyRxZG+ufEhzTaGnVfPHYl4lYChwTnWs5iaP9cY/HLbbloQBCPbc\nFoAZOxHhRH9yzUWAY9fmus3iBKuKyowXLvBIYpL33X7/wdZtxVNDeZmPM02EaZJgL2PrG9Lrk7Xf\nc5Prwdhnt/d9/QMb0HLTkAXxyUb4v/MmQ6qcFbVByt5Dger2J7Vx3wIDAQABAoIBAQCNKLGqsXgn\np03eBRXsbhsSpDmkRT8CyBAVfB9jMuQRpZP7RXWsdW1BMOnxkQum2HqSTaDq/nc10qNenUkS1zGR\nvrU+dXYYJViscm42CVzqCxBYFz7OpMp585A6dUbNKQ/hi8gDbHMhd3lAzUjBBIYgY80OKuvHVbUP\nk3/GduF6GK9RvNWE3gOn4Fwcti5LcXM+XAhlODpHM5moi2mGc5FpGW0XVlw/PYI6OIle3fRcs5oS\nXgrAMc3ZUMFQBWT1C+ON4ciFZplkx5VzkBJvsVAsGHmUw6lA18ZyZY85pmjebGAarR7dKoYMc/7o\nnfEnqyteAX+uap1vAMPHY48CGiOpAoGBAO7WR2lvXpO+ZbI+13dX5+BEk3C8/q00rC5NRIL81Dli\nScIfkJa8wrIQ4MacTAffODLx/XstqIHDzCZPn5+F2jxR2MvLJJhLVNecCrrnOoc3wg4T56YLEDJK\nwXeguRrIpuAY1cvuwD/4FNMeMznW6rLzO9iDSY7YEUR6j7wvCfa7AoGBALMU41w1TWJ5w4fjgVjA\naAOakHzfEwQoKLTNk9TUFyuW2s0vOxeLsQC0uXIuJqxkCLf914gvMCC3E7efShMke3Nwx6AanVbG\nYuNcCbF7jqYZZyXzDqwvEvd17jDfVpMzjyZeNaLendqI0ZPbu+q2ure+ZACRfXLjkgyk0TCKkYkt\nAoGBAMGeUswlre1mF7l1emVUjTThQwnNHUU7Ay6SEy2BW2adyJ+nySZ6JfgG+MHp6TUkf8HFLbIT\nJJz2JuUT/Zpr/6yQyYkEyUjxIPmdlZFJbMg7qoSRHGUeaOXsdoTNdeJBsg7P81pc1dvF+NPhkrOc\nCC5EQ0rVu+ciQHVcSnPLSm+LAoGBAKPPO5og98RYdU9b//lbJsoFHJ3cv9xRj8qzMcLAruEkgMin\np3oGQcbPicK0DJQsfP0wqXLdFz0VumeAwZV5DaOvSitdrQavOc2XZxZ+WyH8oqnIB2fhN4w8x8DU\nyrCjvRHvo0h0MhiwhmSl3uL4exuQgJE0hHgPSo1eRiwM1MuRAoGAaljrAomufUzvTkduGno1vc23\nbY4fahtbhzf4mTUjvmZhERWHeOlSaioEvhcSVikzCDTn8eyFwFmURLACCNK+RqkjK26+sfL+W5IL\nGIGD1r9TqoBNNQoZnmTBYfkIo7lLYSr6/eZovqGRG7KtJSBAvjP7jb3ptgJJ9av7pfTuGQ0=\n-----END RSA PRIVATE KEY-----\n"
end
