import requests
from bs4 import BeautifulSoup
import time
import random

# ✅ 配置后端接口地址
API_URL = "http://localhost:8081/api/movies"

# ✅ 更加丰富的请求头池，模拟真实用户行为
USER_AGENTS = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Edge/119.0.0.0",
    "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1"
]

def get_headers():
    return {
        'User-Agent': random.choice(USER_AGENTS),
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'Referer': 'https://movie.douban.com/top250'
    }

def get_sentiment(rating):
    """根据评分自动判定情感倾向"""
    if rating >= 8.5: return "正面"
    elif rating >= 6.5: return "中性"
    else: return "负面"

def get_movie_detail(movie_url):
    """
    深入电影详情页获取真实的长评/短评和分类标签
    """
    try:
        # 详情页访问频率限制
        time.sleep(random.uniform(1.0, 2.5))
        response = requests.get(movie_url, headers=get_headers(), timeout=10)
        if response.status_code != 200:
            return None
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # 1. 抓取真实的短评 (豆瓣详情页 hot comments 第一条)
        comment_tag = soup.find('span', class_='short')
        review = comment_tag.get_text().strip().replace('\n', '') if comment_tag else "经典之作，不仅是电影，更是艺术。"
        
        # 2. 抓取详细类型标签 (如: 剧情 / 犯罪)
        genre_tags = soup.find_all('span', property='v:genre')
        tags = ",".join([g.get_text() for g in genre_tags]) if genre_tags else "经典"
        
        return {"review": review, "tags": tags}
    except Exception as e:
        print(f"⚠️ 详情页抓取失败: {e}")
        return None

def crawl_douban_top250():
    """
    全量抓取豆瓣 Top 250 电影
    """
    print("🚀 开始执行豆瓣 Top 250 全量数据采集任务...")
    
    # Top 250 共 10 页，每页 25 条
    for page in range(0, 250, 25):
        url = f"https://movie.douban.com/top250?start={page}"
        print(f"\n--- 正在爬取第 {int(page/25) + 1} 页 ({url}) ---")
        
        try:
            response = requests.get(url, headers=get_headers(), timeout=15)
            if response.status_code != 200:
                print(f"❌ 访问受限 (状态码: {response.status_code})。建议更换 IP 或增加休眠。")
                break
                
            soup = BeautifulSoup(response.text, 'html.parser')
            items = soup.find_all('div', class_='item')
            
            for item in items:
                # 提取列表页基础信息
                title = item.find('span', class_='title').get_text()
                rating = float(item.find('span', class_='rating_num').get_text())
                detail_url = item.find('a')['href']
                
                print(f"🔎 正在处理: {title} ({rating}分)")
                
                # 详情页深度采集
                detail_info = get_movie_detail(detail_url)
                
                if detail_info:
                    movie_data = {
                        "title": title,
                        "rating": rating,
                        "sentiment": get_sentiment(rating),
                        "tags": detail_info['tags'],
                        "review": detail_info['review']
                    }
                    # 实时同步至 Java 后端
                    sync_to_backend(movie_data)
                
            # 每页爬完后进行较长休眠，防止触发反爬机制
            sleep_time = random.uniform(5, 10)
            print(f"😴 本页完成，为了安全休眠 {sleep_time:.1f} 秒...")
            time.sleep(sleep_time)
            
        except Exception as e:
            print(f"💥 列表页抓取异常: {e}")

def sync_to_backend(movie):
    """将数据推送到 Spring Boot 控制器接口"""
    try:
        resp = requests.post(API_URL, json=movie)
        if resp.status_code in [200, 201]:
            print(f"✅ 已存入数据库: {movie['title']}")
        else:
            print(f"⚠️ 接口报错: {movie['title']} ({resp.status_code})")
    except Exception as e:
        print(f"🚫 后端连接失败，请检查服务是否启动。")

if __name__ == "__main__":
    start_time = time.time()
    crawl_douban_top250()
    end_time = time.time()
    
    duration = (end_time - start_time) / 60
    print(f"\n🏁 全量采集任务结束。总耗时: {duration:.1f} 分钟。")